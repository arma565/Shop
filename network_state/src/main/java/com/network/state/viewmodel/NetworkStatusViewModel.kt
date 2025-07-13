package com.network.state.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.network.state.Const
import com.network.state.NetworkConnectivityListener
import com.network.state.NetworkMonitorManager
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext

class NetworkStatusViewModel : ViewModel() {
    private val _isServerResponding: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isServerResponding = _isServerResponding.asStateFlow()

    private val _isConnected: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    suspend fun start(context: Context) {
        withContext(IO) {
            NetworkMonitorManager(
                context,
                Const.SERVER_URL,
                object : NetworkConnectivityListener {
                    override fun onNetworkConnected(state: Boolean) {
                        _isConnected.value = state
                    }

                    override fun onServerResponse(state: Boolean) {
                        _isServerResponding.value = state
                    }
                }
            ).checkNetworkState()
        }
    }
}