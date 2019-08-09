package com.example.ukrainianholes.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.media.ExifInterface
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import java.io.File

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

fun Int.toDp() = (this / Resources.getSystem().displayMetrics.density).toInt()

fun Int.toPx() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Bitmap.saveToFile(file: File, orientation: String? = null): Boolean {
    val compressed = this.compress(Bitmap.CompressFormat.JPEG, 100, file.outputStream())

    val exif = ExifInterface(file.absolutePath)
    exif.setAttribute(ExifInterface.TAG_ORIENTATION, orientation)
    exif.saveAttributes()

    return compressed
}