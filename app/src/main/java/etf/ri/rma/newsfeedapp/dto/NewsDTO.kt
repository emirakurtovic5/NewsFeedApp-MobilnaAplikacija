package etf.ri.rma.newsfeedapp.dto

import com.google.gson.annotations.SerializedName
import etf.ri.rma.newsfeedapp.model.News
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.model.Tags
import java.text.SimpleDateFormat
import java.util.Locale

data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val articles: List<NewsArticleDTO>
)

data class NewsArticleDTO(
    @SerializedName("uuid") val uuid: String,
    @SerializedName("title") val title: String,
    @SerializedName("snippet") val snippet: String,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("categories") val category: List<String>,
    @SerializedName("source") val source: String,
    @SerializedName("published_at") val publishedDate: String
)

fun NewsArticleDTO.toNewsItem(): NewsItem {
    val formattedDate = try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val date = inputFormat.parse(this.publishedDate)
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(date)
    } catch (e: Exception) {
        this.publishedDate
    }

    val news = News(
        uuid = this.uuid,
        title = this.title,
        snippet = this.snippet,
        imageUrl = this.imageUrl,
        category = this.category.firstOrNull()?.lowercase() ?: "general",
        isFeatured = false,
        source = this.source,
        publishedDate = formattedDate
    )

    val tags = this.category.mapIndexed { index, tag ->
        Tags(id = index, value = tag)
    }

    return NewsItem(
        news = news,
        tags = tags
    )
}