package etf.ri.rma.newsfeedapp.data.network.api


import etf.ri.rma.newsfeedapp.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApiService {
    @GET("v1/news/top")
    suspend fun getTopStories(
        @Query("api_token") apiToken: String,
        @Query("categories") categories: String,
        @Query("limit") limit: Int,
        @Query("locale") locale: String = "us"
    ): NewsResponse

    @GET("v1/news/similar/{uuid}")
    suspend fun getSimilarStories(
        @Path("uuid") uuid: String,
        @Query("api_token") apiToken: String,
    ): NewsResponse
}

