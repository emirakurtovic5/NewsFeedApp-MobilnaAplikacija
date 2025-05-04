package etf.ri.rma.newsfeedapp.screen

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AppNavHost(navController: NavHostController) {
    val filterViewModel: FilterViewModel = viewModel()

    var dateRange by remember { mutableStateOf<Pair<String?, String?>>(null to null) }
    var unwantedWords by remember { mutableStateOf(listOf<String>()) }
    var selectedCategory by remember { mutableStateOf("Sve") }

    NavHost(navController = navController, startDestination = "/newsfeed") {
        composable("/newsfeed") {
            NewsFeedScreen(
                navigateToFilterScreen = { navController.navigate("/filters") },
                filters = Triple(selectedCategory, dateRange, unwantedWords),
                onNewsClick = { newsId -> navController.navigate("/details/$newsId") },
                filterViewModel = filterViewModel
            )
        }
        composable("/details/{id}") { backStackEntry ->
            val newsId = backStackEntry.arguments?.getString("id") ?: return@composable
            NewsDetailsScreen(
                newsId = newsId,
                onBackToNewsFeed = { navController.popBackStack("/newsfeed", inclusive = false) },
                onRelatedNewsClick = { relatedNewsId -> navController.navigate("/details/$relatedNewsId") }
            )
        }
        composable("/filters") {
            FilterScreen(
                filterViewModel = filterViewModel,
                selectedCategory = selectedCategory,
                onCategorySelected = { category -> selectedCategory = category },
                dateRange = dateRange,
                onDateRangeSelected = { startDate, endDate -> dateRange = startDate to endDate },
                unwantedWords = unwantedWords,
                onUnwantedWordAdded = { word -> unwantedWords = unwantedWords + word },
                onApplyFilters = { category, range, words ->
                    selectedCategory = category
                    dateRange = range as Pair<String?, String?>
                    unwantedWords = words as List<String>
                    navController.popBackStack()
                },
                onBackPressed = { navController.popBackStack() }
            )
        }
    }
}