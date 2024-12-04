package com.network.state

internal interface INetworkStateManager {
    fun checkNetworkState(listener : IResponseEvent)
}