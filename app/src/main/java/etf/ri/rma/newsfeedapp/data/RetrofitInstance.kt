package etf.ri.rma.newsfeedapp.data

import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL_NEWS = "https://api.thenewsapi.com/"
    private const val BASE_URL_IMAGGA = "https://api.imagga.com/"

    private val retrofitNews by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_NEWS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val newsApi: NewsApiService by lazy {
        retrofitNews.create(NewsApiService::class.java)
    }

    private val retrofitImagga by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_IMAGGA)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val imaggaApi: ImagaApiService by lazy {
        retrofitImagga.create(ImagaApiService::class.java)
    }
}