package com.store.shop.data.model

data class BaseProductCategory(
    val products: List<Product> = listOf(),
    val `data`: Data = Data()
)