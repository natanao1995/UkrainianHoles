package com.example.ukrainianholes.data.remote.entity


import com.google.gson.annotations.SerializedName

data class AddHoleRequest(
    @SerializedName("address")
    val address: String,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("lat")
    val lat: Float,
    @SerializedName("lng")
    val lng: Float,
    @SerializedName("photos")
    val photos: List<Long>
)