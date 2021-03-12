package es.heathcliff.ajanitestbot.service

import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody

data class SendHttpCommandRequest(val url: String, val method: String, val body: String?)

class HttpService {
    private val client = OkHttpClient()

    fun send(request: SendHttpCommandRequest): String {
        val httpRequest = Request.Builder()
            .url(request.url)
            .method(
                request.method,
                if (request.body != null) RequestBody.create(
                    MediaType.parse("application/json"),
                    request.body
                ) else null
            ).build()

        val response = client.newCall(httpRequest).execute()
        val responseBody = response.body()?.string()

        return responseBody ?: ""
    }
}