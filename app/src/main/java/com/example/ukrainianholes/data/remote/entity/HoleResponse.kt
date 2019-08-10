package com.example.ukrainianholes.data.remote.entity


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HoleResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("address")
    val address: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("fixed_photos")
    val fixedPhotos: List<FixedPhoto>,
    @SerializedName("inserted_at")
    val insertedAt: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("like")
    val like: Boolean,
    @SerializedName("likes_number")
    val likesNumber: Int,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("photos")
    val photos: List<Photo>,
    @SerializedName("status")
    val status: Status,
    @SerializedName("updated_at")
    val updatedAt: Long
): Parcelable