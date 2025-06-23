package etf.ri.rma.newsfeedapp.model

import androidx.room.*

@Entity(tableName = "News")
data class News(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val uuid: String,
    val title: String,
    val snippet: String,
    val imageUrl: String?,
    val category: String,
    val isFeatured: Boolean,
    val source: String,
    val publishedDate: String
)

@Entity(tableName = "Tags")
data class Tags(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val value: String
)

@Entity(tableName = "NewsTags", primaryKeys = ["newsId", "tagsId"])
data class NewsTags(
    val newsId: Int,
    val tagsId: Int
)