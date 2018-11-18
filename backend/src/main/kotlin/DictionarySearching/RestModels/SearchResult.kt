package DictionarySearching.RestModels

data class SearchResult(
        var meta: SearchResultMeta,
        var data: List<SearchResultData>
)