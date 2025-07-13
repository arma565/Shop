package com.network.state.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import com.network.state.NetworkConnectivityListener

class NetworkConnectivityChecker(
    context: Context,
    private val listener: NetworkConnectivityListener
) {

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var networkCallback: ConnectivityManager.NetworkCallback? = null
    private var isRegistered = false

    /**
     * Checks the current network connectivity status.
     *
     * This method will immediately check the network status and notify the listener.
     * It also registers a network callback to monitor connectivity changes.
     */
    fun checkNetworkConnectivity() {
        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
        val isConnected =
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
        listener.onNetworkConnected(isConnected)

        // Register for network updates if not already registered
        if (!isRegistered) {
            registerNetworkCallback()
        }
    }

    /**
     * Registers a network callback to monitor network connectivity changes.
     */
    private fun registerNetworkCallback() {
        if (isRegistered) {
            return
        }
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.onNetworkConnected(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.onNetworkConnected(false)
            }
        }

        networkCallback?.let {
            connectivityManager.registerNetworkCallback(networkRequest, it)
            isRegistered = true
        }


    }

    /**
     * Unregisters the network callback if it is currently registered.
     */
    fun unregisterNetworkCallback() {
        if (isRegistered && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback!!)
            networkCallback = null
            isRegistered = false
        }
    }
}