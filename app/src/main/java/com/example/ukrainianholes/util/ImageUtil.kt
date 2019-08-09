package com.example.ukrainianholes.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory

object ImageUtil {
    fun getBitmapDescriptor(context: Context, id: Int, width: Int, height: Int): BitmapDescriptor? {
        context.getDrawable(id)?.let {
            val w = width.toPx()
            val h = height.toPx()
            it.setBounds(0, 0, w, h)
            val bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            it.draw(canvas)
            return BitmapDescriptorFactory.fromBitmap(bm)
        }
        return null
    }

    fun compressImage(context: Context, imageUri: Uri, maxSideSize: Int): Bitmap {
        val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))

        if (bitmap.height <= maxSideSize && bitmap.width <= maxSideSize)
            return bitmap

        var height = maxSideSize
        var width = maxSideSize
        if (bitmap.height > bitmap.width) {
            width = (width * bitmap.width.toFloat() / bitmap.height).toInt()
        } else {
            height = (height * bitmap.height.toFloat() / bitmap.width).toInt()
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, false)
    }
}