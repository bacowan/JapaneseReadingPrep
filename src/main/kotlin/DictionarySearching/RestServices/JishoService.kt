package DictionarySearching.RestServices

import DictionarySearching.RestModels.SearchResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JishoService {

    @GET("search/words/")
    fun getSearchResults(
            @Query("keyword") keyword: String
    ): Call<SearchResult>
}