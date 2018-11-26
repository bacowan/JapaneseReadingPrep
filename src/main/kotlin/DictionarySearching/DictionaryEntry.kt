package DictionarySearching

data class DictionaryEntry(
        val word: String,
        val reading: String,
        val english: MutableList<EnglishMeaning>,
        val isCommon: Boolean
)

data class EnglishMeaning(val meaning: String, val partsOfSpeech: List<String>)