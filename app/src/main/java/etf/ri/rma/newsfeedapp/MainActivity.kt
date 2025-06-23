package etf.ri.rma.newsfeedapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import etf.ri.rma.newsfeedapp.data.NewsDatabase
import etf.ri.rma.newsfeedapp.data.network.NewsDAO
import etf.ri.rma.newsfeedapp.data.RetrofitInstance
import etf.ri.rma.newsfeedapp.data.network.ImagaDAO
import etf.ri.rma.newsfeedapp.screen.AppNavHost
import etf.ri.rma.newsfeedapp.ui.theme.NewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val database = NewsDatabase.getDatabase(this)
        setContent {
            NewsTheme {
                val navController = rememberNavController()
                val newsDAO: NewsDAO = NewsDAO(RetrofitInstance.newsApi)
                val imaggaDAO: ImagaDAO = ImagaDAO(RetrofitInstance.imaggaApi)
                AppNavHost(navController = navController, newsDAO = newsDAO, imaggaDAO = imaggaDAO, database = database)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNewsFeedScreen() {
    NewsTheme {
        val navController = rememberNavController()
        val newsDAO: NewsDAO = NewsDAO(RetrofitInstance.newsApi)
        val imaggaDAO: ImagaDAO = ImagaDAO(RetrofitInstance.imaggaApi)
        val context = androidx.compose.ui.platform.LocalContext.current
        val database = NewsDatabase.getDatabase(context)
        AppNavHost(navController = navController, newsDAO = newsDAO, imaggaDAO = imaggaDAO, database = database)
    }
}