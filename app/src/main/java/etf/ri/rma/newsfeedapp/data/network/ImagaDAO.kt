package etf.ri.rma.newsfeedapp.data.network

import android.util.Patterns
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import android.util.Base64
import android.util.Log
import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ImagaDAO(private val api: ImagaApiService) {
    private val cache = mutableMapOf<String, List<String>>()
    private val mutex = Mutex()
    var credentialsToEncode: String =
        "acc_ea0ece8cfe164af" + ":" + "af1c90bf68cad80c664e93d9f3420c1a"
    val basicAuth = "Basic " + Base64.encodeToString(credentialsToEncode.toByteArray(), Base64.NO_WRAP)

    suspend fun getTags(imageURL: String): List<String> {
        Log.d("ImagaDAO", "Checking URL: $imageURL")
        if (!isValidURL(imageURL)) {
            Log.e("ImagaDAO", "Invalid URL format: $imageURL")
            throw InvalidImageURLException("Neispravan URL slike")
        }

        return withContext(Dispatchers.IO) {
            mutex.withLock {
                cache[imageURL]?.let {
                    Log.d("ImagaDAO", "Cache hit for URL: $imageURL")
                    return@withContext it
                }

                try {
                    Log.d("ImagaDAO", "Making API call for URL: $imageURL")
                    val response = api.getTags(basicAuth, imageURL)
                    val tags = response.result.tags.map { it.tag.en }
                    Log.d("ImagaDAO", "Fetched tags for URL $imageURL: $tags")
                    cache[imageURL] = tags
                    return@withContext tags
                } catch (e: Exception) {
                    Log.e("ImagaDAO", "Error fetching tags for URL $imageURL: ${e.message}")
                    throw e
                }
            }
        }
    }

    private fun isValidURL(url: String): Boolean {
        val isValid = Patterns.WEB_URL.matcher(url).matches()
        Log.d("ImagaDAO", "URL validation for $url: $isValid")
        return isValid
    }
}


