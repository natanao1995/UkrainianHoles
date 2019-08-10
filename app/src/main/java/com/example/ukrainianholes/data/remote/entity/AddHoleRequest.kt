package com.example.ukrainianholes.data.remote.entity


import com.google.gson.annotations.SerializedName

data class AddHoleRequest(
    @SerializedName("address")
    var address: String? = null,
    @SerializedName("comment")
    var comment: String? = null,
    @SerializedName("lat")
    var lat: Double? = null,
    @SerializedName("lng")
    var lng: Double? = null,
    @SerializedName("photos")
    var photos: MutableList<Long> = mutableListOf(),
    @SerializedName("accident_rate")
    var accidentRate: Int? = null
) {
    fun isFilled(): Boolean = address != null && comment != null && lat != null && lng != null && accidentRate != null
}