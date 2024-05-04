package com.store.shop.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BaseHome(
    val news: List<New> = listOf(),
    val mobile: List<Product> = listOf(),
    val makeup: List<Product> = listOf(),
    val discount: List<Product> = listOf(),
    @SerializedName("AmazingOffer")
    @Expose
    val amazingOffer: List<Product> = listOf(),
    val `data`: Data = Data()
)