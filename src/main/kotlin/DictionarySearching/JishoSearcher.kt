package DictionarySearching

import DictionarySearching.RestModels.SearchResultData
import DictionarySearching.RestServices.JishoService
import com.atilika.kuromoji.ipadic.Token
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JishoSearcher {

    val japanesePosToEnglish = hashMapOf(
            "名詞" to listOf("Noun, Place"),
            "動詞" to listOf("Verb"),
            "形容詞" to listOf("Adjective"),
            "副詞" to listOf("Adverb"),
            "助詞" to listOf("Particle"),
            "接続詞" to listOf("Conjugation"),
            "助動詞" to listOf("Pronoun"),
            "連体詞" to listOf("Pre-noun adjectival", "I-adjective", "No-adjective"),
            "感動詞" to listOf("Interjection")
    )

    fun searchMostLikely(token: Token): DictionaryEntry {
        val response = getSearchResponse(token.baseForm)
        return getBestMatchFromResponse(response, token)
    }

    fun searchAll(token: Token): List<DictionaryEntry> {
        val response = getSearchResponse(token.baseForm)
        return response.map { toDictionaryEntry(it) }
    }

    private fun getSearchResponse(searchQuery: String): List<SearchResultData> {
        val httpClient = OkHttpClient.Builder().build()
        val retrofit = Retrofit.Builder()
                .baseUrl("https://jisho.org/api/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
        val jishoService = retrofit.create(JishoService::class.java)
        val callSync = jishoService.getSearchResults(searchQuery)
        return callSync.execute().body()?.data ?: listOf()
    }

    private fun getBestMatchFromResponse(results: List<SearchResultData>, searchedToken: Token): DictionaryEntry {
        val asDictionaryEntries = results.map { toDictionaryEntry(it) }
        val filteredByJapanese = filterByJapanese(asDictionaryEntries, searchedToken)
        val filteredByPartOfSpeech = filterByPartOfSpeech(filteredByJapanese, searchedToken)
        val filteredByCommon = filterByCommon(filteredByPartOfSpeech)
        return filteredByCommon.first()
    }

    private fun toDictionaryEntry(data: SearchResultData): DictionaryEntry {
        return DictionaryEntry(
                word = data.japanese.first().word,
                reading = data.japanese.first().reading,
                isCommon = data.is_common,
                english = data.senses.map { EnglishMeaning(
                        meaning = it.english_definitions.joinToString(),
                        partsOfSpeech = it.parts_of_speech
                ) }.toMutableList()
        )
    }

    private fun filterByJapanese(itemsToFilter: List<DictionaryEntry>, token: Token): List<DictionaryEntry> {
        val filteredOnBaseForm = itemsToFilter.filter { it.word == token.baseForm }
        return if (filteredOnBaseForm.isNotEmpty()) {
            filteredOnBaseForm
        }
        else {
            val filteredOnReading = itemsToFilter.filter { it.reading == token.reading }
            return if (filteredOnBaseForm.isNotEmpty()) {
                filteredOnReading
            }
            else {
                itemsToFilter
            }
        }
    }

    private fun filterByPartOfSpeech(itemsToFilter: List<DictionaryEntry>, token: Token): List<DictionaryEntry> {
        val filteredByPartOfSpeech = itemsToFilter.filter {
            containsPartOfSpeech(it.english.flatMap { e -> e.partsOfSpeech }, token)
        }
        return if (filteredByPartOfSpeech.isNotEmpty()) {
            filteredByPartOfSpeech.forEach { entry ->
                val meaningsWithCorrectPartOfSpeech = entry.english.filter {
                    containsPartOfSpeech(it.partsOfSpeech, token)
                }
                entry.english.clear()
                entry.english.addAll(meaningsWithCorrectPartOfSpeech)
            }
            filteredByPartOfSpeech
        }
        else {
            itemsToFilter
        }
    }

    private fun containsPartOfSpeech(partsOfSpeech: List<String>, token: Token): Boolean {
        val englishPos = japanesePosToEnglish[token.partOfSpeechLevel1]
        return englishPos == null || partsOfSpeech.any { resultPos ->
            englishPos.any { it.startsWith(resultPos, ignoreCase = true) }
        }
    }

    private fun filterByCommon(itemsToFilter: List<DictionaryEntry>): List<DictionaryEntry> {
        val filteredOnCommon = itemsToFilter.filter { it.isCommon }
        return if (filteredOnCommon.isNotEmpty()) {
            filteredOnCommon
        }
        else {
            itemsToFilter
        }
    }
}