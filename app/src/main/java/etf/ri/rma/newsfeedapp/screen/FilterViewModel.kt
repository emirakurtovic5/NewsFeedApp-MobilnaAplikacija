package etf.ri.rma.newsfeedapp.screen

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FilterViewModel : ViewModel() {
    private val _selectedCategory = MutableStateFlow("Sve")
    val selectedCategory: StateFlow<String> get() = _selectedCategory

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

}
