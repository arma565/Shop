package com.store.shop.helper.constants

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object GlobalFunctions {

    sealed class OperationStatus {
        data class Success<out T>(val response: T) : OperationStatus()
        data class Error(val errorMessage: String, val throwable: Throwable? = null) :
            OperationStatus()

        object Loading : OperationStatus()
    }

    fun getStateUI(
        state: OperationStatus,
        onLoading: () -> Unit,
        onDone: () -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        when (state) {
            is OperationStatus.Success<*> -> {
                Log.d("TAG", "getStateUI: Done")
                onDone()
            }

            is OperationStatus.Error -> {
                Log.d("TAG", "getStateUI: Error: ${state.errorMessage}")
                onError(state.errorMessage)
            }

            is OperationStatus.Loading -> {
                Log.d("TAG", "getStateUI: Loading...")
                onLoading()
            }
        }
    }

    @Composable
    fun IndeterminateLinearIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }

    @Composable
    fun IndeterminateCircularIndicator() {
        Box(
            modifier = Modifier
                .fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}