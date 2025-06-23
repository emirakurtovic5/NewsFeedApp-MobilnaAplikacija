package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import etf.ri.rma.newsfeedapp.data.NewsDatabase
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFeedScreen(
    navigateToFilterScreen: (Any?) -> Unit = {},
    filters: Triple<String, Pair<String?, String?>, List<String>> = Triple("Sve", Pair(null, null), emptyList()),
    onNewsClick: (String) -> Unit = {},
    filterViewModel: FilterViewModel,
    newsDAO: NewsDAO,
    database: NewsDatabase? = null
) {
    val selectedCategory by filterViewModel.selectedCategory.collectAsState()
    val (dateRange, unwantedWords) = filters.second to filters.third
    val allNews = remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var filteredNews by remember { mutableStateOf(allNews.value) }
    var isLoading by remember { mutableStateOf(false) }
    val categoryMap = mapOf(
        "Sve" to null,
        "Politika" to "politics",
        "Sport" to "sports",
        "Nauka" to "science",
        "Tehnologija" to "tech",
        "Zabava" to "entertainment",
    )
    val context = LocalContext.current


    val dateFormatter = remember {
        SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    }


    LaunchedEffect(selectedCategory) {
        isLoading = true
        try {

            val localNews = if (selectedCategory == "Sve") {
                database?.savedNewsDAO()?.getAllNewsWithTags() ?: emptyList()
            } else {
                val backendCategory = categoryMap[selectedCategory]
                database?.savedNewsDAO()?.getNewsWithCategory(backendCategory ?: "") ?: emptyList()
            }


            if (isOnline(context)) {
                try {
                    val onlineNews = if (selectedCategory == "Sve") {
                        newsDAO.getAllStories()
                    } else {
                        newsDAO.getTopStoriesByCategory(categoryMap[selectedCategory] ?: "general")
                    }


                    onlineNews.forEach { newsItem ->
                        database?.savedNewsDAO()?.saveNews(newsItem)
                    }


                    val combinedNews = (localNews + onlineNews).distinctBy { it.news.uuid }


                    allNews.value = if (selectedCategory != "Sve") {

                        val sortedNews = combinedNews.sortedByDescending {
                            it.news.publishedDate?.let { date ->
                                try { dateFormatter.parse(date) } catch (e: Exception) { null }
                            }
                        }

                        sortedNews.mapIndexed { index, item ->
                            if (index < 3) {
                                item.copy(news = item.news.copy(isFeatured = true))
                            } else {
                                item.copy(news = item.news.copy(isFeatured = false))
                            }
                        }
                    } else {

                        combinedNews.map { item ->
                            item.copy(news = item.news.copy(isFeatured = false))
                        }
                    }
                } catch (e: Exception) {
                    Log.e("NewsFeed", "Error fetching online news", e)
                    allNews.value = localNews.map { item ->
                        item.copy(news = item.news.copy(isFeatured = false))
                    }
                }
            } else {
                allNews.value = localNews.map { item ->
                    item.copy(news = item.news.copy(isFeatured = false))
                }
            }

            filteredNews = allNews.value
        } catch (e: Exception) {
            Log.e("NewsFeed", "Error loading news", e)
            allNews.value = emptyList()
            filteredNews = emptyList()
        } finally {
            isLoading = false
        }
    }


    LaunchedEffect(allNews.value, dateRange, unwantedWords) {
        try {
            filteredNews = allNews.value.filter { newsItem ->

                val dateMatches = try {
                    val newsDate = newsItem.news.publishedDate ?: ""
                    when {
                        dateRange.first != null && dateRange.second != null -> {
                            val newsDateObj = dateFormatter.parse(newsDate)
                            val startDateObj = dateFormatter.parse(dateRange.first)
                            val endDateObj = dateFormatter.parse(dateRange.second)
                            newsDateObj in startDateObj..endDateObj
                        }
                        dateRange.first != null -> {
                            val newsDateObj = dateFormatter.parse(newsDate)
                            val startDateObj = dateFormatter.parse(dateRange.first)
                            newsDateObj >= startDateObj
                        }
                        dateRange.second != null -> {
                            val newsDateObj = dateFormatter.parse(newsDate)
                            val endDateObj = dateFormatter.parse(dateRange.second)
                            newsDateObj <= endDateObj
                        }
                        else -> true
                    }
                } catch (e: Exception) {
                    true
                }


                val wordsMatch = unwantedWords.all { word ->
                    !newsItem.news.title.contains(word, ignoreCase = true) &&
                            !newsItem.news.snippet.contains(word, ignoreCase = true)
                }

                dateMatches && wordsMatch
            }
        } catch (e: Exception) {
            Log.e("NewsFeed", "Error filtering news", e)
            filteredNews = allNews.value
        }
    }


    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val categories = listOf("Sve", "Politika", "Sport", "Nauka", "Tehnologija", "Zabava")
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { filterViewModel.updateCategory(category) },
                            label = { Text(category) }
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = { navigateToFilterScreen(selectedCategory) },
                        label = { Text("Više filtera ...") }
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Učitavanje vijesti...")
                }
            } else if (filteredNews.isEmpty()) {
                MessageCard(message = "Nema pronađenih vijesti u kategoriji $selectedCategory")
            } else {
                NewsList(
                    newsItems = filteredNews,
                    onNewsClick = onNewsClick
                )
            }
        }
    }
}

