package DictionarySearching.RestModels

data class SearchResultSense(
        var english_definitions: List<String>,
        var parts_of_speech: List<String>,
        var info: List<String>
)