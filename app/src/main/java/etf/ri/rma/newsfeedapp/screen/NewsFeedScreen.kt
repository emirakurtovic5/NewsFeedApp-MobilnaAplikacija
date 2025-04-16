package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsData
import etf.ri.rma.newsfeedapp.model.NewsItem
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NewsFeedScreen() {

    val allNews = remember { NewsData.getAllNews() }
    val categories = listOf("Sve", "Politika", "Sport", "Nauka/tehnologija", "Moda")
    var selectedCategory by remember { mutableStateOf("Sve") }
    var filteredNews by remember { mutableStateOf(allNews) }
    var sortAscending by remember { mutableStateOf(false) }
    var sortDescending by remember { mutableStateOf(false) }

    LaunchedEffect(selectedCategory, sortAscending, sortDescending) {
        filteredNews = if (selectedCategory == "Sve") {
            allNews
        } else {
            allNews.filter { it.category == selectedCategory }
        }

        filteredNews = when {
            sortAscending -> filteredNews.sortedBy { it.publishedDate }
            sortDescending -> filteredNews.sortedByDescending { it.publishedDate }
            else -> filteredNews
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
                            onClick = { selectedCategory = category },
                            label = { Text(category) },
                            modifier = Modifier.testTag(tag)
                        )
                    }
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = sortAscending,
                    onClick = {
                        sortAscending = !sortAscending
                        if (sortAscending) sortDescending = false
                    },
                    label = { Text("Datum ⇩") },
                    modifier = Modifier.testTag("sort_chip_date_asc")
                )
                FilterChip(
                    selected = sortDescending,
                    onClick = {
                        sortDescending = !sortDescending
                        if (sortDescending) sortAscending = false
                    },
                    label = { Text("Datum ⇧") },
                    modifier = Modifier.testTag("sort_chip_date_desc")
                )
            }


            if (filteredNews.isEmpty()) {
                MessageCard(message = "Nema pronađenih vijesti u kategoriji $selectedCategory")
            } else {
                NewsList(
                    newsItems = filteredNews,
                    modifier = Modifier.testTag("news_list")
                )
            }
        }
    }
}