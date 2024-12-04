package com.network.state

import android.content.Context
import com.network.state.service.ConnectivityCheck

class NetworkStateManager(private val context: Context) : INetworkStateManager {
    override fun checkNetworkState(listener: IResponseEvent) {
        ConnectivityCheck(context,listener).check(context)
    }
}