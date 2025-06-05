package etf.ri.rma.newsfeedapp

import java.text.SimpleDateFormat
import java.util.Locale

fun formatNewsDate(originalDate: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return try {
        val date = inputFormat.parse(originalDate)
        date?.let { outputFormat.format(it) } ?: originalDate
    } catch (e: Exception) {
        originalDate // Return original if parsing fails
    }
}