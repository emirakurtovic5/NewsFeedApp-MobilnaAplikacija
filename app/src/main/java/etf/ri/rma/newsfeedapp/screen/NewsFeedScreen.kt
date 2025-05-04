package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.layout.Arrangement
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
import etf.ri.rma.newsfeedapp.data.NewsData
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.testTag
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFeedScreen(
    navigateToFilterScreen: (Any?) -> Unit = {},
    filters: Triple<String, Pair<String?, String?>, List<String>> = Triple("Sve", Pair(null, null), emptyList()),
    onNewsClick: (String) -> Unit = {},
    filterViewModel: FilterViewModel
) {
    val selectedCategory by filterViewModel.selectedCategory.collectAsState()
    val (dateRange, unwantedWords) = filters.second to filters.third
    val allNews = remember { NewsData.getAllNews() }
    var filteredNews by remember { mutableStateOf(allNews) }


    LaunchedEffect(selectedCategory, dateRange, unwantedWords) {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        filteredNews = allNews.filter { newsItem ->
            val isCategoryMatch = selectedCategory == "Sve" || newsItem.category == selectedCategory
            val isDateInRange = try {
                val newsDate = dateFormat.parse(newsItem.publishedDate)
                val startDate = dateRange.first?.let { dateFormat.parse(it) }
                val endDate = dateRange.second?.let { dateFormat.parse(it) }
                (startDate == null || newsDate >= startDate) && (endDate == null || newsDate <= endDate)
            } catch (e: Exception) {
                true
            }
            val isUnwantedWordAbsent = unwantedWords.isEmpty() || unwantedWords.none { word ->
                newsItem.title.contains(word, ignoreCase = true)
            }

            isCategoryMatch && isDateInRange && isUnwantedWordAbsent
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
                    val categories = listOf("Sve", "Politika", "Sport", "Nauka/tehnologija", "Moda")
                    categories.forEach { category ->
                        val tag = when (category) {
                            "Sve" -> "filter_chip_all"
                            "Politika" -> "filter_chip_pol"
                            "Sport" -> "filter_chip_spo"
                            "Nauka/tehnologija" -> "filter_chip_sci"
                            "Moda" -> "filter_chip_moda"
                            else -> "filter_chip_none"
                        }
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { filterViewModel.updateCategory(category) },
                            label = { Text(category) },
                            modifier = Modifier.testTag(tag)
                        )
                    }
                    FilterChip(
                        selected = false,
                        onClick = { navigateToFilterScreen(selectedCategory) },
                        label = { Text("Više filtera ...") },
                        modifier = Modifier.testTag("filter_chip_more")
                    )
                }
            }


            if (filteredNews.isEmpty()) {
                MessageCard(message = "Nema pronađenih vijesti u kategoriji $selectedCategory")
            } else {
                NewsList(
                    newsItems = filteredNews,
                    onNewsClick = onNewsClick,
                    modifier = Modifier.testTag("news_list")
                )
            }
        }
    }
}