package etf.ri.rma.newsfeedapp.screen

import FeaturedNewsCard
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import etf.ri.rma.newsfeedapp.model.NewsItem
import androidx.compose.ui.platform.testTag

@Composable
fun NewsList(newsItems: List<NewsItem>, onNewsClick: (String) -> Unit, modifier: Modifier = Modifier) {
    Log.d("NewsList", "Number of items: ${newsItems.size}")
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("news_list"),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(newsItems) { newsItem ->
            if (newsItem.isFeatured) {
                FeaturedNewsCard(news = newsItem, onClick = { onNewsClick(newsItem.uuid) })
            } else {
                StandardNewsCard(news = newsItem, onClick = { onNewsClick(newsItem.uuid) })
            }
        }
    }
}