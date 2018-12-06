package Server

import com.atilika.kuromoji.ipadic.Token

const val SYMBOL = "Symbol"

class WordFactory {
    // Japanese parts of speech mapped to potential synonyms.
    // The direct translation is first, and the others
    // are potential things that may pop up for POS when
    // searching
    private val japanesePosToEnglish = hashMapOf(
            "名詞" to listOf("Noun", "Place"),
            "動詞" to listOf("Verb"),
            "形容詞" to listOf("Adjective"),
            "副詞" to listOf("Adverb"),
            "助詞" to listOf("Particle"),
            "接続詞" to listOf("Conjugation"),
            "助動詞" to listOf("Pronoun"),
            "連体詞" to listOf("I-adjective", "Pre-noun adjectival", "No-adjective"),
            "感動詞" to listOf("Interjection"),
            "一般" to listOf("Simple"),
            "記号" to listOf(SYMBOL),
            "空白" to listOf("Blank"),
            "固有名詞" to listOf("Proper Noun"),
            "人名" to listOf("Person's Name", "Name"),
            "姓" to listOf("Surname", "Last Name", "Family Name"),
            "自立" to listOf("Independent"),
            "組織" to listOf("Organization"),
            "接頭詞" to listOf("Prefix"),
            "名詞接続" to listOf("Connector Noun"),
            "連体化" to listOf("Consolidation"),
            "サ変接続" to listOf("Irregular Conjugation"),
            "非自立" to listOf("Not Independent"),
            "副詞可能" to listOf("Adverb"),
            "格助詞" to listOf("Case Marking Particle"),
            "句点" to listOf("Period"),
            "数" to listOf("Quantity", "Amount", "Several", "Count"),
            "接続助詞" to listOf("Conjunctive Particle"),
            "接尾" to listOf("Suffix"),
            "助数詞" to listOf("Counter"),
            "助詞類接続" to listOf("Connecting Particle Mark"),
            "引用" to listOf("Quotation"),
            "副助詞" to listOf("Adverbial Particle"),
            "係助詞" to listOf("Binding Particle", "Linking Particle", "Connecting Particle"),
            "読点" to listOf("Comma"),
            "代名詞" to listOf("Pronoun"),
            "名" to listOf("Name")
    )

    fun createWord(token: Token) : Word {
        return Word(
                token.baseForm,
                token.pronunciation,
                listOf(
                        getEnglishFromJapanese(token.partOfSpeechLevel1),
                        getEnglishFromJapanese(token.partOfSpeechLevel2),
                        getEnglishFromJapanese(token.partOfSpeechLevel3),
                        getEnglishFromJapanese(token.partOfSpeechLevel4))
                        .filter { it != "*" })
    }

    private fun getEnglishFromJapanese(japanese: String): String {
        return japanesePosToEnglish.getOrDefault(
                japanese,
                listOf(japanese)
        ).first()
    }
}