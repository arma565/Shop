package com.store.shop.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val icon: String = "",
) : Parcelable