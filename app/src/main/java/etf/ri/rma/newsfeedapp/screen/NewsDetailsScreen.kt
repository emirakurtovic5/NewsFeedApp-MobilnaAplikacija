package etf.ri.rma.newsfeedapp.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.data.NewsData
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun NewsDetailsScreen(
    newsId: String,
    onBackToNewsFeed: () -> Unit,
    onRelatedNewsClick: (String) -> Unit
) {
    val newsItem = NewsData.getAllNews().find { it.id == newsId }
    val relatedNews = newsItem?.let { currentNews ->
        NewsData.getAllNews()
            .filter { it.category == currentNews.category && it.id != currentNews.id }
            .sortedWith(compareBy(
                { kotlin.math.abs(dateDifference(it.publishedDate, currentNews.publishedDate)) },
                { it.title }
            ))
            .take(2)
    } ?: emptyList()


    BackHandler {
        onBackToNewsFeed()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (newsItem != null) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = newsItem.title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.testTag("details_title")
                )
                Text(
                    text = newsItem.snippet,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.testTag("details_snippet")
                )
                Text(
                    text = "Kategorija: ${newsItem.category}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag("details_category")
                )
                Text(
                    text = "Izvor: ${newsItem.source}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag("details_source")
                )
                Text(
                    text = "Datum objave: ${newsItem.publishedDate}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.testTag("details_date")
                )


                if (relatedNews.isNotEmpty()) {
                    Text(
                        text = "Povezane vijesti iz iste kategorije:",
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    relatedNews.forEachIndexed { index, relatedItem ->
                        Text(
                            text = relatedItem.title,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .testTag("related_news_title_${index + 1}")
                                .clickable { onRelatedNewsClick(relatedItem.id) }
                        )
                    }
                }


                Button(
                    onClick = { onBackToNewsFeed() },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .testTag("details_close_button")
                ) {
                    Text(text = "Zatvori detalje")
                }
            }
        } else {
            Text(
                text = "Vijest nije pronađena.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


private fun dateDifference(date1: String, date2: String): Long {
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val parsedDate1 = dateFormat.parse(date1)
        val parsedDate2 = dateFormat.parse(date2)
        kotlin.math.abs(parsedDate1.time - parsedDate2.time)
    } catch (e: Exception) {
        Long.MAX_VALUE
    }
}