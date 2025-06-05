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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.testTag
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFeedScreen(
    navigateToFilterScreen: (Any?) -> Unit = {},
    filters: Triple<String, Pair<String?, String?>, List<String>> = Triple("Sve", Pair(null, null), emptyList()),
    onNewsClick: (String) -> Unit = {},
    filterViewModel: FilterViewModel,
    newsDAO: NewsDAO
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

    LaunchedEffect(selectedCategory) {
        isLoading = true
        try {
            allNews.value = if (selectedCategory == "Sve") {
                newsDAO.getAllStories().distinctBy { it.uuid }
            } else {
                val backendCategory = categoryMap[selectedCategory] ?: throw IllegalArgumentException("Invalid category")
                val topStories = newsDAO.getTopStoriesByCategory(backendCategory)
                val allCategoryStories = newsDAO.getAllStories().filter { it.category == backendCategory }
                (topStories + allCategoryStories).distinctBy { it.uuid }
            }
            filteredNews = allNews.value
        } catch (e: Exception) {
            Log.e("NewsFeedScreen", "Error fetching news: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    LaunchedEffect(allNews.value, dateRange, unwantedWords) {
        try {
            filteredNews = allNews.value.filter { newsItem ->
                val newsDateStr = newsItem.publishedDate ?: ""

                val isWithinDateRange = when {
                    dateRange.first != null && dateRange.second != null ->
                        newsDateStr >= dateRange.first!! && newsDateStr <= dateRange.second!!

                    dateRange.first != null ->
                        newsDateStr >= dateRange.first!!

                    dateRange.second != null ->
                        newsDateStr <= dateRange.second!!

                    else -> true
                }

                val doesNotContainUnwantedWords = unwantedWords.all { word ->
                    !newsItem.title.contains(word, ignoreCase = true) &&
                            !newsItem.snippet.contains(word, ignoreCase = true)
                }

                isWithinDateRange && doesNotContainUnwantedWords
            }
        } catch (e: Exception) {
            Log.e("NewsFeedScreen", "Filter error: ${e.message}")
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
                        val tag = when (category) {
                            "Sve" -> "filter_chip_all"
                            "Politika" -> "filter_chip_pol"
                            "Sport" -> "filter_chip_spo"
                            "Nauka" -> "filter_chip_sci"
                            "Tehnologija" -> "filter_chip_tech"
                            "Zabava" -> "filter_chip_zabava"
                            else -> "filter_chip_none"
                        }
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = {
                                Log.d("NewsFeedScreen", "Category updated to: $category")
                                filterViewModel.updateCategory(category)
                            },
                            label = { Text(category) },
                            modifier = Modifier.testTag(tag)
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = {
                            try {
                                Log.d("NewsFeedScreen", "Navigating to filter screen with category: $selectedCategory")
                                navigateToFilterScreen(selectedCategory)
                            } catch (e: Exception) {
                                Log.e("NewsFeedScreen", "Error navigating to filter screen: ${e.message}")
                            }
                        },
                        label = { Text("Više filtera ...") },
                        modifier = Modifier.testTag("filter_chip_more")
                    )
                }
            }

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Učitavanje vijesti...",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else if (filteredNews.isEmpty()) {
                Log.d("NewsFeedScreen", "No news found for category: $selectedCategory")
                MessageCard(message = "Nema pronađenih vijesti u kategoriji $selectedCategory")
            } else {
                Log.d("NewsFeedScreen", "Displaying news list with ${filteredNews.size} items")
                filteredNews.forEach { newsItem ->
                    Log.d("NewsFeedScreen", "News Item: ${newsItem.title}, Category: ${newsItem.category}")
                }
                NewsList(
                    newsItems = filteredNews,
                    onNewsClick = onNewsClick,
                    modifier = Modifier.testTag("news_list")
                )
            }
        }
    }
}