package com.store.shop.data.model


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gallery(
    @SerializedName("img")
    @Expose
    val img: String = ""
) : Parcelable