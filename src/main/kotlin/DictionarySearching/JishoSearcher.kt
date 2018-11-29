package DictionarySearching

import DictionarySearching.RestModels.SearchResultData
import DictionarySearching.RestServices.JishoService
import Server.Word
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class JishoSearcher {

    fun searchMostLikely(word: Word): DictionaryEntry {
        val response = getSearchResponse(word.base)
        return getBestMatchFromResponse(response, word)
    }

    fun searchAll(word: Word): List<DictionaryEntry> {
        val response = getSearchResponse(word.base)
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

    private fun getBestMatchFromResponse(results: List<SearchResultData>, searchedWord: Word): DictionaryEntry {
        val asDictionaryEntries = results.map { toDictionaryEntry(it) }
        val filteredByJapanese = filterByJapanese(asDictionaryEntries, searchedWord)
        val filteredByPartOfSpeech = filterByPartOfSpeech(filteredByJapanese, searchedWord)
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

    private fun filterByJapanese(itemsToFilter: List<DictionaryEntry>, word: Word): List<DictionaryEntry> {
        val filteredOnBaseForm = itemsToFilter.filter { it.word == word.base }
        return if (filteredOnBaseForm.isNotEmpty()) {
            filteredOnBaseForm
        }
        else {
            val filteredOnReading = itemsToFilter.filter { it.reading == word.reading }
            return if (filteredOnBaseForm.isNotEmpty()) {
                filteredOnReading
            }
            else {
                itemsToFilter
            }
        }
    }

    private fun filterByPartOfSpeech(itemsToFilter: List<DictionaryEntry>, word: Word): List<DictionaryEntry> {
        val filteredByPartOfSpeech = itemsToFilter.filter {
            containsPartOfSpeech(it.english.flatMap { e -> e.partsOfSpeech }, word)
        }
        return if (filteredByPartOfSpeech.isNotEmpty()) {
            filteredByPartOfSpeech.forEach { entry ->
                val meaningsWithCorrectPartOfSpeech = entry.english.filter {
                    containsPartOfSpeech(it.partsOfSpeech, word)
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

    private fun containsPartOfSpeech(partsOfSpeech: List<String>, word: Word): Boolean {
        return partsOfSpeech.any { resultPos ->
            word.partsOfSpeech.any { it.startsWith(resultPos, ignoreCase = true) }
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