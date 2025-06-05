package etf.ri.rma.newsfeedapp.data.network

import android.util.Patterns
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import android.util.Base64
import etf.ri.rma.newsfeedapp.data.network.api.ImagaApiService
import etf.ri.rma.newsfeedapp.data.network.exception.InvalidImageURLException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ImagaDAO(private val api: ImagaApiService) {
    private val cache = mutableMapOf<String, List<String>>()
    private val mutex = Mutex()
    var credentialsToEncode: String =
        "acc_92638e16d15a583" + ":" + "b45bd727defb04d6cfa1234c42f408d4"
    val basicAuth = "Basic " + Base64.encodeToString(credentialsToEncode.toByteArray(), Base64.NO_WRAP)

    suspend fun getTags(imageURL: String): List<String> {
        if (!isValidURL(imageURL)) throw InvalidImageURLException("Neispravan URL slike")
        return withContext(Dispatchers.IO) {
            mutex.withLock {
                cache[imageURL]?.let { return@withContext it }

                val response = api.getTags(basicAuth, imageURL)
                val tags = response.result.tags.map { it.tag.en }
                cache[imageURL] = tags
                return@withContext tags
            }
        }
    }

    private fun isValidURL(url: String): Boolean {
        return Patterns.WEB_URL.matcher(url).matches()
    }
}

