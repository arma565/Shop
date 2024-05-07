package com.network.state

interface INetworkStateManager {

    fun connect(listener : IResponseEvent<Boolean>)

    fun checkNetwork(listener : IResponseEvent<Boolean>)

}