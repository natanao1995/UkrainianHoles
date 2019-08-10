package com.example.ukrainianholes.data.remote.entity

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FixedPhoto(
    @SerializedName("id")
    val id: Long
): Parcelable