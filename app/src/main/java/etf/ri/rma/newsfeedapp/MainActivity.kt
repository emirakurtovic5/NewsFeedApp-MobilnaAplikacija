package etf.ri.rma.newsfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import etf.ri.rma.newsfeedapp.model.NewsItem
import etf.ri.rma.newsfeedapp.screen.NewsFeedScreen
import etf.ri.rma.newsfeedapp.ui.theme.NewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NewsTheme {

                NewsFeedScreen()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewNewsFeedScreen() {
    NewsTheme {
        
        NewsFeedScreen()
    }
}