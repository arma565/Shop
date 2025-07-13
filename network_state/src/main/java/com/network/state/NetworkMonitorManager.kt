package com.network.state

import android.content.Context
import com.network.state.server.Ping
import com.network.state.service.NetworkConnectivityChecker

class NetworkMonitorManager(
    private val context: Context,
    private val server: String = "",
    private val networkListener: NetworkConnectivityListener? = null
) : INetworkStateManager {

    override fun checkNetworkState() {
        NetworkConnectivityChecker(context, networkListener!!).checkNetworkConnectivity()
        Ping(server, networkListener).pingServer()
    }

    override fun unregisterNetworkCallback() {
        NetworkConnectivityChecker(context, networkListener!!).unregisterNetworkCallback()
    }
}