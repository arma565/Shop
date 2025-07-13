package com.network.state
  interface NetworkConnectivityListener {
    fun onNetworkConnected(state : Boolean)
    fun onServerResponse(state : Boolean)
}