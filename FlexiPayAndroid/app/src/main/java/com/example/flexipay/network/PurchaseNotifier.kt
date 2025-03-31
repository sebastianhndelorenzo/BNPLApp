package com.example.flexipay.network

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.*

object PurchaseNotifier {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()
    private var serverUrl: String = ""
    private val scope = CoroutineScope(Dispatchers.IO + Job())
    private val JSON = "application/json; charset=utf-8".toMediaType()

    fun initialize(url: String) {
        serverUrl = url
    }

    fun notifyPurchase(service: String, duration: String) {
        val json = JSONObject().apply {
            put("service", service)
            put("duration", duration)
            put("status", "purchased")
            put("timestamp", System.currentTimeMillis())
        }

        val request = Request.Builder()
            .url(serverUrl)
            .post(
                RequestBody.create(
                    JSON,
                    json.toString()
                )
            )
            .build()

        scope.launch {
            try {
                client.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        println("Purchase notification sent successfully")
                    } else {
                        println("Failed to send purchase notification: ${response.code}")
                    }
                }
            } catch (e: Exception) {
                println("Error sending purchase notification: ${e.message}")
            }
        }
    }

    fun cleanup() {
        scope.cancel()
    }
} 