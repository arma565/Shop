package com.network.state.server

import com.network.state.NetworkConnectivityListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.IOException
import java.util.concurrent.TimeUnit

/**
 * The `Ping` class provides functionality to check the connectivity status of a server.
 * It sends a simple HTTP request to the specified server URL and determines if the connection
 * was successful based on the server's response.
 */
class Ping(private val serverUrl: String,private val networkConnectivityListener: NetworkConnectivityListener) {
    /**
     * Interface for handling server connection events.
     */
    fun pingServer() {
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(10L, TimeUnit.SECONDS)
            .readTimeout(10L, TimeUnit.SECONDS)
            .build()

        val httpRequest = Request.Builder()
            .url(serverUrl)
            .build()

        httpClient.newCall(httpRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                networkConnectivityListener.onServerResponse(false)
            }

            override fun onResponse(call: Call, response: Response) {
                networkConnectivityListener.onServerResponse(true)
            }
        })
    }
}