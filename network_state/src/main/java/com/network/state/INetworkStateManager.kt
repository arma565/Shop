package com.network.state

internal interface INetworkStateManager {
    fun checkNetworkState()
    fun unregisterNetworkCallback()
}