package etf.ri.rma.newsfeedapp.screen

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.dto.NewsArticleDTO
import etf.ri.rma.newsfeedapp.model.NewsItem
import java.text.SimpleDateFormat
import java.util.Locale
import etf.ri.rma.newsfeedapp.dto.toNewsItem

@Composable
fun NewsDetailsScreen(
    newsId: String,
    onBackToNewsFeed: () -> Unit,
    onRelatedNewsClick: (String) -> Unit,
    newsDAO: NewsDAO,
    imaggaDAO: ImagaDAO
) {
    var newsItem by remember { mutableStateOf<NewsItem?>(null) }
    var relatedNews by remember { mutableStateOf<List<NewsItem>>(emptyList()) }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorState by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(newsId) {
        try {
            Log.d("NewsDetailsScreen", "Loading news details for UUID: $newsId")
            val allStories = newsDAO.getAllStories()
            newsItem = allStories.find { it.uuid == newsId }

            if (newsItem == null) {
                Log.e("NewsDetailsScreen", "News not found for UUID: $newsId")
            } else {
                Log.d("NewsDetailsScreen", "Found news item: ${newsItem!!.title}")
            }
            relatedNews = newsDAO.getSimilarStories(newsId)
            Log.d("NewsDetailsScreen", "Related news: ${relatedNews.map { it.title }}")

            tags = newsItem?.imageUrl?.let { url ->
                try {
                    imaggaDAO.getTags(url)
                } catch (e: Exception) {
                    listOf("Invalid image URL")
                }
            } ?: emptyList()

            isLoading = false
        } catch (e: Exception) {
            errorState = "Error loading data: ${e.localizedMessage}"
            isLoading = false
        }
    }

    BackHandler { onBackToNewsFeed() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            isLoading -> LoadingState()
            errorState != null -> ErrorState(errorState!!)
            newsItem == null -> NotFoundState()
            else -> ContentState(
                newsItem!!,
                relatedNews,
                tags,
                onBackToNewsFeed,
                onRelatedNewsClick
            )
        }
    }
}

@Composable
private fun ContentState(
    newsItem: NewsItem,
    relatedNews: List<NewsItem>,
    tags: List<String>,
    onBack: () -> Unit,
    onRelatedClick: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = newsItem.title,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.testTag("details_title")
            )
        }
        item{
            newsItem.imageUrl?.let { url ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(url)
                        .crossfade(true)
                        .build(),
                    contentDescription = "News image",
                    modifier = Modifier
                        .height(200.dp)
                        .testTag("details_image"),
                    contentScale = ContentScale.Crop
                )
            }

        }
        item {
            Text(
                text = newsItem.snippet,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        item {
            MetadataSection(newsItem)
        }

        item {
            TagsSection(tags)
        }

        if (relatedNews.isNotEmpty()) {
            item {
                Text(
                    text = "Povezane vijesti:",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            items(relatedNews) { item ->
                RelatedNewsItem(item, onRelatedClick)
            }
        }


        item {
            Button(
                onClick = onBack,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("details_close_button")
            ) {
                Text("Zatvori detalje")
            }
        }
    }
}

@Composable
private fun MetadataSection(newsItem: NewsItem) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text("Kategorija: ${newsItem.category}")
        Text("Izvor: ${newsItem.source}")
        Text("Datum: ${newsItem.publishedDate}")
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TagsSection(tags: List<String>) {
    if (tags.isNotEmpty()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "Tagovi slike:",
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                tags.forEach { tag ->
                    Text(
                        text = "#$tag",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.testTag("details_tag_$tag")
                    )
                }
            }
        }
    }
}

@Composable
private fun RelatedNewsItem(item: NewsItem, onClick: (String) -> Unit) {
    Text(
        text = item.title,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .clickable {
                Log.d("RelatedNewsItem", "Clicked on related news with UUID: ${item.uuid}")
                onClick(item.uuid)
            }
            .testTag("related_news_${item.uuid}")
    )
}

@Composable
private fun LoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Učitavam detalje...")
    }
}

@Composable
private fun ErrorState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun NotFoundState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text("Vijest nije pronađena")
    }
}

private fun dateDifference(date1: String, date2: String): Long {
    return try {
        val format = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val d1 = format.parse(date1)
        val d2 = format.parse(date2)
        kotlin.math.abs((d1?.time ?: 0) - (d2?.time ?: 0))
    } catch (e: Exception) {
        Long.MAX_VALUE
    }
}