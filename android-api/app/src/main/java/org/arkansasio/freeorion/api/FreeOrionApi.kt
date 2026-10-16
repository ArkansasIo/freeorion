package org.arkansasio.freeorion.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

data class ApiResult(val ok: Boolean, val message: String, val body: JSONObject? = null)

class FreeOrionApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .build()

    suspend fun health(): ApiResult = get("health")
    suspend fun gameInfo(): ApiResult = get("api/v1/game")

    private suspend fun get(path: String): ApiResult = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(ApiConfig.BASE_URL.trimEnd('/') + "/" + path.trimStart('/'))
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                val text = response.body?.string().orEmpty()
                val json = runCatching { JSONObject(text) }.getOrNull()
                if (response.isSuccessful) {
                    ApiResult(true, "HTTP ${response.code}", json)
                } else {
                    ApiResult(false, "Server returned HTTP ${response.code}", json)
                }
            }
        } catch (e: Exception) {
            ApiResult(false, e.message ?: "Unable to reach API")
        }
    }
}
