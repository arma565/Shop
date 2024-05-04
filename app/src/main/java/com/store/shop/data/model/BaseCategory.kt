package com.store.shop.data.model

data class BaseCategory(
    val mobile: List<Product> = listOf(),
    val makeup: List<Product> = listOf(),
    val mode: List<Product> = listOf(),
    val sport: List<Product> = listOf(),
    val home: List<Product> = listOf(),
    val `data`: Data = Data()
)