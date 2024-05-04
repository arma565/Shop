package com.store.shop.data.model


import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class New(
    val icon: String = "",
    val id: String = "",
    val link: String = "",
    val title: String = "",
    val type: String = ""
) : Parcelable