package etf.ri.rma.newsfeedapp.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class NewsItem(
    @Embedded val news: News,
    @Relation(
        parentColumn = "id",
        entity = Tags::class,
        entityColumn = "id",
        associateBy = Junction(
            value = NewsTags::class,
            parentColumn = "newsId",
            entityColumn = "tagsId"
        )
    )
    val tags: List<Tags>
) {

    val title: String
        get() = news.title

    val uuid: String
        get() = news.uuid

    val snippet: String
        get() = news.snippet

    val category: String
        get() = news.category

    val imageUrl: String?
        get() = news.imageUrl

    val isFeatured: Boolean
        get() = news.isFeatured

    val publishedDate: String
        get() = news.publishedDate

    val source: String
        get() = news.source


    val imageTags: List<Tags>
        get() = tags


    constructor(
        uuid: String,
        title: String,
        snippet: String,
        category: String,
        imageUrl: String?,
        isFeatured: Boolean,
        publishedDate: String,
        source: String
    ) : this(
        news = News(
            uuid = uuid,
            title = title,
            snippet = snippet,
            category = category,
            imageUrl = imageUrl,
            isFeatured = isFeatured,
            publishedDate = publishedDate,
            source = source
        ),
        tags = emptyList()
    )
}
