package com.store.shop.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val catId: String = "",
    val catName: String = "",
    val title: String = "",
    val brand: String = "",
    val garanti: String = "",
    val count: String = "",
    val shortDescription: String = "",
    val fullDescription: String = "",
    val special: String = "",
    val discount: String = "",
    val rate: String = "",
    val price: String = "",
    val icon: String = "",
    val gallery: List<Gallery> = listOf(),
) : Parcelable