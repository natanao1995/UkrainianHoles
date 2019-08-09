package com.example.ukrainianholes.data.remote.entity

import com.google.gson.annotations.SerializedName

data class CreateHoleRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("photos")
    val photos: List<Long>
)