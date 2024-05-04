package com.store.shop.data.model


data class BaseCategories(
    val brands: List<Brand> = listOf(),
    val categories: List<Category> = listOf(),
    val `data`: Data = Data()
)