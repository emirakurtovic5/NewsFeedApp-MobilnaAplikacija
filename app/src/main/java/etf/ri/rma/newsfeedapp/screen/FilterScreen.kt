package etf.ri.rma.newsfeedapp.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterScreen(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    dateRange: Pair<String?, String?>,
    onDateRangeSelected: (String?, String?) -> Unit,
    unwantedWords: List<String>,
    onUnwantedWordAdded: (String) -> Unit,
    onApplyFilters: (String, Pair<String?, String?>, List<String>) -> Unit,
    filterViewModel: FilterViewModel,
    onBackPressed: () -> Unit
) {
    val categories = listOf("Sve", "Politika", "Sport", "Nauka/tehnologija", "Moda")
    val currentCategory by filterViewModel.selectedCategory.collectAsState()
    var tempSelectedCategory by remember { mutableStateOf(currentCategory) }
    var showDatePicker by remember { mutableStateOf(false) }
    var unwantedWordInput by remember { mutableStateOf("") }

    if (showDatePicker) {
        DateRangePickerDialog(
            onDismissRequest = { showDatePicker = false },
            onDateSelected = { startDate, endDate ->
                onDateRangeSelected(startDate, endDate)
                showDatePicker = false
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Text("Kategorije", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
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
                        selected = tempSelectedCategory == category,
                        onClick = {
                            tempSelectedCategory = category
                            onCategorySelected(category)
                        },
                        label = { Text(category) },
                        modifier = Modifier.testTag(tag),
                        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary,
                            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                            containerColor = MaterialTheme.colorScheme.surface,
                            labelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text("Opseg datuma", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = dateRange.let { "${it.first ?: "N/A"};${it.second ?: "N/A"}" },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("filter_daterange_display"),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.testTag("filter_daterange_button"),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Odaberi")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Text("Nepoželjne riječi", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextField(
                    value = unwantedWordInput,
                    onValueChange = { unwantedWordInput = it },
                    placeholder = { Text("Unesite riječ") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("filter_unwanted_input"),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                Button(
                    onClick = {
                        val word = unwantedWordInput.trim()
                        if (word.isNotEmpty() && unwantedWords.none { it.equals(word, ignoreCase = true) }) {
                            onUnwantedWordAdded(word)
                            unwantedWordInput = ""
                        }
                    },
                    modifier = Modifier.testTag("filter_unwanted_add_button"),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Dodaj")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))


            Column(modifier = Modifier.testTag("filter_unwanted_list")) {
                unwantedWords.forEach { word ->
                    Text(text = word, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }


        Button(
            onClick = {
                filterViewModel.updateCategory(tempSelectedCategory)
                onApplyFilters(tempSelectedCategory, dateRange, unwantedWords)
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(16.dp)
                .testTag("filter_apply_button"),
            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Primijeni filtere")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    onDismissRequest: () -> Unit,
    onDateSelected: (String?, String?) -> Unit
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val datePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = {
                    val startDate = datePickerState.selectedStartDateMillis?.let { millis ->
                        LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000)).format(dateFormatter)
                    }
                    val endDate = datePickerState.selectedEndDateMillis?.let { millis ->
                        LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000)).format(dateFormatter)
                    }
                    onDateSelected(startDate, endDate)
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(onClick = onDismissRequest) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(state = datePickerState)
    }
}