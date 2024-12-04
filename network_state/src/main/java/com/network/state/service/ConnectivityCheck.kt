package com.network.state.service

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.network.state.IResponseEvent

class ConnectivityCheck(private val listener: IResponseEvent) {

    private var connectivityManager: ConnectivityManager? = null

    fun check(context: Context) {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        val network = connectivityManager?.activeNetwork
        val networkCapabilities = connectivityManager?.getNetworkCapabilities(network)
        val isNetworkConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        if (isNetworkConnected){
            listener.onConnected(true)
            ConnectivityReceiver(listener).register(context)
        }else{
            listener.onConnected(false)
            ConnectivityReceiver(listener).unregister()
        }
    }
}