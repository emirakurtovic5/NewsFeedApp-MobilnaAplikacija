package etf.ri.rma.newsfeedapp.screen

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class NewsFeedViewModel : ViewModel() {
    var selectedCategory by mutableStateOf("Sve")
        private set

    fun updateSelectedCategory(category: String) {
        selectedCategory = category
    }
}