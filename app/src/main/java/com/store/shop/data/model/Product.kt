package com.store.shop.data.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    var productId: Int = 0,
    var id: String = "",
    var catId: String = "",
    var catName: String = "",
    var title: String = "",
    var brand: String = "",
    var garanti: String = "",
    var count: String = "",
    var shortDescription: String = "",
    var fullDescription: String = "",
    var special: String = "",
    var discount: String = "",
    var rate: String = "",
    var price: String = "",
    var icon: String = "",
    @Ignore var gallery: List<Gallery> = listOf(),
) : Parcelable