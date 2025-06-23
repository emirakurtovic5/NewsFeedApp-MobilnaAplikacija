package etf.ri.rma.newsfeedapp.data.network

import android.util.Log
import kotlinx.coroutines.withTimeout
import etf.ri.rma.newsfeedapp.data.network.api.NewsApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidUUIDException
import etf.ri.rma.newsfeedapp.dto.toNewsItem
import etf.ri.rma.newsfeedapp.model.News
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.model.Tags
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.*

class NewsDAO(private val api: NewsApiService) {
    private val cache = mutableMapOf<String, Pair<Long, List<NewsItem>>>()
    private val allStories = mutableListOf<NewsItem>()
    private val mutex = Mutex()
    //private val apiToken = "QDsG0KSt7PbQKriRCpT22wlJsVZ1pSCJTZDFUraH"
    private val apiToken = "R207Z8dkvM0j4cbUQnAXThuInWs79cpqiBU3wivw"
    private val imageTagsCache = mutableMapOf<String, List<String>>()
    private val similarStoriesCache = mutableMapOf<String, List<NewsItem>>()
    val predefinedStories = listOf(
        NewsItem(
            news = News(
                id = 1,
                uuid = "7ad689ab-aeef-4cef-b070-b80d6c15ae1c",
                title = "Zachary Levi claims supporting Trump made him a Hollywood outsider",
                snippet = "NEW You can now listen to Fox News articles!\n\nZachary Levi considers himself a Hollywood outsider because of his political beliefs.\n\nThe \"Shazam!\" star says the...",
                imageUrl = "https://static.foxnews.com/foxnews.com/content/uploads/2025/05/zachary-levi-donald-trump.jpg",
                category = "sports",
                isFeatured = false,
                source = "foxnews.com",
                publishedDate = "27-05-2025"
            ),
            tags = listOf(Tags(17, "general"), Tags(19, "politics"))
        ),
        NewsItem(
            news = News(
                id = 2,
                uuid = "adf94614-28b2-44c0-b1fa-8d2666f62749",
                title = "NPR sues Trump over executive order cutting federal funding",
                snippet = "Create your free profile or log in to save this article\n\nCreate your free profile or log in to save this article\n\nNational Public Radio on Tuesday sued Presiden...",
                imageUrl = "https://i.abcnewsfe.com/a/bf0ba1f6-6d85-4eb7-9651-22f6dca1d20d/wirestory_f320314b30df6934238a5b6af47c5067_16x9.jpg?w=1600",
                category = "politics",
                isFeatured = false,
                source = "nbcnews.com",
                publishedDate = "28-05-2025"
            ),
            tags = listOf(Tags(15, "general"), Tags(16, "politics"))
        ),
        NewsItem(
            news = News(
                id = 3,
                uuid = "8bf497c1-2f22-47f8-aaf5-10f384f05162",
                title = "Trump admin asking federal agencies to cancel remaining Harvard contracts",
                snippet = "The Trump administration is asking all federal agencies to find ways to terminate all federal contracts with Havard University amid an ongoing standoff over for...",
                imageUrl = "https://static.foxnews.com/foxnews.com/content/uploads/2025/05/harvard-palestine-protest.jpg",
                category = "science",
                isFeatured = false,
                source = "foxnews.com",
                publishedDate = "29-05-2025"
            ),
            tags = listOf(Tags(13, "general"), Tags(14, "politics"))
        ),
        NewsItem(
            news = News(
                id = 4,
                uuid = "d58eb628-f7ec-4bfa-b39e-680cd712dc72",
                title = "As Putin’s Russia brutally hammers Ukraine, Trump confronts put-up-or-shut-up test",
                snippet = "Since returning to the White House, Donald Trump has repeatedly vouched for Russia’s Vladimir Putin, assuring the world that his counterpart in Moscow is sinc...",
                imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2025-02/250221-Trump-Putin-Helsinki-2018-aa-1213-31d6a1.jpg",
                category = "politics",
                isFeatured = false,
                source = "msnbc.com",
                publishedDate = "30-05-2025"
            ),
            tags = listOf(Tags(1, "general"), Tags(2, "politics"))
        ),
        NewsItem(
            news = News(
                id = 5,
                uuid = "d4a0c325-b316-4211-926c-a5d0c925fa0d",
                title = "Trump administration seeks to end all federal contracts with Harvard",
                snippet = "The Trump administration intends to ask all federal agencies to seek ways to end their contracts with Harvard University, a senior administration official told ...",
                imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/mpx/2704722219/2025_05/1748351875354_n_mj_harvard_250527_1920x1080-2huji5.jpg",
                category = "science",
                isFeatured = false,
                source = "msnbc.com",
                publishedDate = "31-05-2025"
            ),
            tags = listOf(Tags(3, "general"), Tags(4, "politics"))
        ),
        NewsItem(
            news = News(
                id = 6,
                uuid = "8f6d27ca-b950-4190-a01b-2a3e8329bdbe",
                title = "Trump Media says it's raising $2.5 billion to buy bitcoin",
                snippet = "LAS VEGAS — Trump Media announced Tuesday a $2.5 billion raise from institutional investors to bankroll one of the largest bitcoin treasury allocations by a p...",
                imageUrl = "https://media-cldnry.s-nbcnews.com/image/upload/t_nbcnews-fp-1200-630,f_auto,q_auto:best/rockcms/2024-11/241118-trump-crypto-conference-vl-430p-a11121.jpg",
                category = "politics",
                isFeatured = false,
                source = "nbcnews.com",
                publishedDate = "01-06-2025"
            ),
            tags = listOf(Tags(5, "general"), Tags(6, "politics"))
        ),
        NewsItem(
            news = News(
                id = 7,
                uuid = "bcdf8831-2f8b-43d1-8705-ba0e36db72dd",
                title = "Trump threatens to withhold California's federal funding over transgender athlete",
                snippet = "US President Donald Trump and First Lady Melania Trump are greeted by California Governor Gavin Newson upon arrival at Los Angeles International Airport in Los ...",
                imageUrl = "https://image.cnbcfm.com/api/v1/image/108092359-1737760460117-gettyimages-2195134171-AFP_36VX2RY.jpeg?v=1737760507&w=1920&h=1080",
                category = "fashion",
                isFeatured = false,
                source = "cnbc.com",
                publishedDate = "02-06-2025"
            ),
            tags = listOf(Tags(7, "general"), Tags(8, "business"))
        ),
        NewsItem(
            news = News(
                id = 8,
                uuid = "0eacb58f-f9d4-4ea8-9043-e29551384cc8",
                title = "Trump to yank all remaining federal funds to Harvard in latest blow to ‘very antisemitic’ Ivy League school: report",
                snippet = "The Trump administration is reportedly planning to cancel all remaining federal contracts with Harvard University — a day after the president said he was pull...",
                imageUrl = "https://nypost.com/wp-content/uploads/sites/2/2025/05/105393656.jpg?quality=75&strip=all&w=1024",
                category = "sports",
                isFeatured = false,
                source = "nypost.com",
                publishedDate = "03-06-2025"
            ),
            tags = listOf(Tags(9, "general"))
        ),
        NewsItem(
            news = News(
                id = 9,
                uuid = "822e53b4-65f9-4017-b199-d0e62bb796aa",
                title = "Trump administration moves to cut federal contracts for Harvard",
                snippet = "The Trump administration is asking federal agencies to cancel remaining contracts with Harvard University\n\nFILE - A relief sculpture rests on a gate to the entr...",
                imageUrl = "https://i.abcnewsfe.com/a/853fa561-df8d-4ae7-bcf6-26e004e1af3f/wirestory_51d2d2618e1f0f5de39cb649644e1dae_16x9.jpg?w=1600",
                category = "sports",
                isFeatured = false,
                source = "abcnews.go.com",
                publishedDate = "26-05-2025"
            ),
            tags = listOf(Tags(10, "general"))
        ),
        NewsItem(
            news = News(
                id = 10,
                uuid = "adebd347-be58-4849-8e6c-3db07cf73fca",
                title = "NPR sues Trump administration over executive order to cut funding to public media",
                snippet = "National Public Radio and three local stations have filed a lawsuit against President Donald Trump, arguing that an executive order aimed at cutting federal fun...",
                imageUrl = "https://i.abcnewsfe.com/a/bf0ba1f6-6d85-4eb7-9651-22f6dca1d20d/wirestory_f320314b30df6934238a5b6af47c5067_16x9.jpg?w=1600",
                category = "fashion",
                isFeatured = false,
                source = "abcnews.go.com",
                publishedDate = "25-05-2025"
            ),
            tags = listOf(Tags(11, "general"), Tags(12, "politics"))
        )
    )

    val categoryMap = mapOf(
        "general" to "Sve",
        "politics" to "Politika",
        "sports" to "Sport",
        "science" to "Nauka",
        "tech" to "Tehnologija",
        "business" to "Biznis",
        "health" to "Zdravlje",
        "entertainment" to "Zabava",
        "fashion" to "Moda"
    )
    val reverseCategoryMap = mapOf(
        "Sve" to "general",
        "Politika" to "politics",
        "Sport" to "sports",
        "Nauka" to "science",
        "Tehnologija" to "tech",
        "Biznis" to "business",
        "Zdravlje" to "health",
        "Zabava" to "entertainment",
        "Moda" to "fashion"
    )

    init {
        allStories.addAll(predefinedStories)
    }
    private val lastFetchTimes = mutableMapOf<String, Long>()
    fun getLastFetchTime(category: String): Long? {
        return lastFetchTimes[category]
    }
    private val lastFetchedStories = mutableMapOf<String, List<NewsItem>>()
    private val lastFeaturedCache = mutableMapOf<String, List<NewsItem>>()
    private val lastFetchTime = mutableMapOf<String, Long>()

    suspend fun getTopStoriesByCategory(category: String): List<NewsItem> {
        val now = System.currentTimeMillis()

        return withContext(Dispatchers.IO) {
            mutex.withLock {
                val lastTime = lastFetchTime[category] ?: 0L
                val elapsed = now - lastTime

                if (lastTime != 0L && elapsed < 30_000) {
                    return@withLock lastFeaturedCache[category] ?: emptyList()
                }
                try {
                    val response = api.getTopStories(
                        apiToken  = apiToken,
                        categories = category,
                        limit     = 3,
                        locale    = "us"
                    )
                    val newlyFetched: List<NewsItem> = response.articles
                        .map { it.toNewsItem() }
                        .take(3)

                    allStories.forEachIndexed { idx, item ->
                        if (item.news.category.equals(category, ignoreCase = true)) {
                            allStories[idx] = item.copy(news = item.news.copy(isFeatured = false))
                        }
                    }
                    val featuredList = mutableListOf<NewsItem>()
                    for (newItem in newlyFetched) {
                        val existingIndex = allStories.indexOfFirst { it.news.uuid == newItem.news.uuid }
                        if (existingIndex != -1) {
                            //val updated = allStories[existingIndex].copy(isFeatured = true)
                            val updated = allStories[existingIndex].copy(news = allStories[existingIndex].news.copy(isFeatured = true))
                            allStories[existingIndex] = updated
                            featuredList.add(updated)
                        } else {
                            //val featured = newItem.copy(isFeatured = true)
                            val featured = newItem.copy(news = newItem.news.copy(isFeatured = true))
                            allStories.add(featured)
                            featuredList.add(featured)
                        }
                    }

                    lastFeaturedCache[category] = featuredList
                    lastFetchTime[category] = now

                    return@withLock featuredList
                } catch (e: Exception) {
                    Log.e("NewsDAO", "Error fetching top stories: ${e.message}")
                    /*val fallback = allStories
                        .filter { it.news.category.equals(category, ignoreCase = true) }
                        .map { it.copy(isFeatured = false) }
                        .take(3)*/val fallback = allStories
                        .filter { it.news.category.equals(category, ignoreCase = true) }
                        .map { it.copy(news = it.news.copy(isFeatured = false)) }
                        .take(3)
                    return@withLock fallback
                }
            }
        }
    }

    suspend fun getAllStories(): List<NewsItem> {
        return withContext(Dispatchers.IO) {
            //allStories.map { it.copy(isFeatured = false) }
            allStories.map { it.copy(news = it.news.copy(isFeatured = false)) }
        }
    }

    suspend fun getSimilarStories(uuid: String): List<NewsItem> {
        Log.d("NewsDAO", "Fetching similar stories for UUID: $uuid")
        if (!isValidUUID(uuid)) {
            Log.e("NewsDAO", "Invalid UUID format: $uuid")
            throw InvalidUUIDException("Invalid UUID format")
        }

        return try {
            withContext(Dispatchers.IO) {
                val response = api.getSimilarStories(uuid, apiToken)
                val similarStories = response.articles
                    .map { it.toNewsItem() }
                    .take(2)


                mutex.withLock {
                    similarStories.forEach { newStory ->
                        if (allStories.none { it.news.uuid == newStory.news.uuid }) {
                            allStories.add(newStory)
                        }
                    }
                }

                similarStories
            }
        } catch (e: Exception) {
            Log.e("NewsDAO", "Error fetching similar stories: ${e.message}")
            emptyList()
        }
    }

    private fun isValidUUID(uuid: String): Boolean {
        return try {
            UUID.fromString(uuid)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
    suspend fun getNewsById(uuid: String): NewsItem? {
        return mutex.withLock {
            allStories.find { it.news.uuid == uuid }
        }
    }
    fun addNewsItem(newsItem: NewsItem) {
        allStories.add(newsItem)
    }

}
