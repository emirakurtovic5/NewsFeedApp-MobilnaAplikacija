package etf.ri.rma.newsfeedapp.data

import android.util.Log
import androidx.room.*
import etf.ri.rma.newsfeedapp.model.News
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.model.NewsTags
import etf.ri.rma.newsfeedapp.model.Tags

@Dao
interface SavedNewsDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNews(news: News): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tags): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewsTag(newsTags: NewsTags)

    @Transaction
    @Query("SELECT * FROM News ORDER BY publishedDate DESC")
    suspend fun getAllNewsWithTags(): List<NewsItem>

    @Transaction
    @Query("SELECT * FROM News WHERE category = :category ORDER BY publishedDate DESC")
    suspend fun getNewsWithCategory(category: String): List<NewsItem>

    @Transaction
    @Query("""
        SELECT DISTINCT n.* FROM News n
        INNER JOIN NewsTags nt ON n.id = nt.newsId
        INNER JOIN Tags t ON nt.tagsId = t.id
        WHERE t.value IN (:tags)
        ORDER BY n.publishedDate DESC
    """)
    suspend fun getNewsByTags(tags: List<String>): List<NewsItem>

    @Query("SELECT * FROM Tags WHERE value = :tagValue LIMIT 1")
    suspend fun getTagByValue(tagValue: String): Tags?

    @Query("""
        SELECT t.value FROM Tags t
        INNER JOIN NewsTags nt ON t.id = nt.tagsId
        WHERE nt.newsId = :newsId
    """)
    suspend fun getTagsForNews(newsId: Int): List<String>

    @Query("SELECT * FROM News WHERE uuid = :uuid LIMIT 1")
    suspend fun getNewsByUuid(uuid: String): News?

    suspend fun saveNews(newsItem: NewsItem): Boolean {
        return try {

            val existingNews = getNewsByUuid(newsItem.news.uuid)


            if (existingNews != null) {
                return false
            }


            val insertedId = insertNews(newsItem.news)
            if (insertedId == -1L) return false


            for (tag in newsItem.tags) {
                val existingTag = getTagByValue(tag.value)
                val tagId = if (existingTag != null) {
                    existingTag.id
                } else {
                    val newTagId = insertTag(tag)
                    if (newTagId == -1L) continue
                    newTagId.toInt()
                }

                insertNewsTag(NewsTags(newsId = insertedId.toInt(), tagsId = tagId))

            }
            true
        } catch (e: Exception) {
            Log.e("SavedNewsDAO", "Error saving news: ${e.message}")
            false
        }
    }

    suspend fun allNews(): List<NewsItem> {
        return getAllNewsWithTags()
    }

    suspend fun addTags(tags: List<String>, newsId: Int): Int {
        return try {
            var newTagsCount = 0
            for (tagValue in tags) {
                val existingTag = getTagByValue(tagValue)
                val tagId = if (existingTag != null) {
                    existingTag.id
                } else {
                    val newTagId = insertTag(Tags(value = tagValue))
                    if (newTagId != -1L) {
                        newTagsCount++
                        newTagId.toInt()
                    } else {
                        continue
                    }
                }


                insertNewsTag(NewsTags(newsId = newsId, tagsId = tagId))
            }
            newTagsCount
        } catch (e: Exception) {
            Log.e("SavedNewsDAO", "Error adding tags: ${e.message}")
            0
        }
    }

    suspend fun getSimilarNews(tags: List<String>): List<NewsItem> {
        return if (tags.isNotEmpty()) {
            getNewsByTags(tags)
        } else {
            emptyList()
        }
    }

    @Query("""
        SELECT t.value FROM Tags t
        INNER JOIN NewsTags nt ON t.id = nt.tagsId
        WHERE nt.newsId = :newsId
    """)
    suspend fun getTags(newsId: Int): List<String>

    @Query("SELECT * FROM Tags")
    abstract suspend fun getAllTags(): List<Tags>
}