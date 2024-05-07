package com.network.state

interface IResponseEvent<T> {
    fun onSuccessful(data: Any)

    fun onFailure(data: Any)
}