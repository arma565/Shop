package com.auth.login.data.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream

/**
 * Convert bitmap(image) to byte array
 * Convert byte array to bitmap(image)
 */
class ImageConverter {

    /**
     * Convert bitmap(image) to byte array
     */
    @TypeConverter
    fun convertBitmapToByteArray(bitmap: Bitmap?) : ByteArray?{
        val outPutStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG , 100,outPutStream)
        return outPutStream.toByteArray()
    }

    /**
     * Convert byte array to bitmap(image)
     */
    @TypeConverter
    fun convertByteArrayToBitmap(byteArray: ByteArray?) : Bitmap?{
        return byteArray?.size?.let { BitmapFactory.decodeByteArray(byteArray , 0 , it) }
    }
}