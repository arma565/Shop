package com.store.shop.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gallery(
    @SerializedName("img")
    @Expose
    val img: String = ""
):Parcelable