package DictionarySearching.RestModels

data class SearchResultData(
        var japanese: List<SearchResultJapanese>,
        var senses: List<SearchResultSense>,
        var is_common: Boolean)