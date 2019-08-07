package com.example.ukrainianholes.data.remote.entity


import com.google.gson.annotations.SerializedName

data class ErrorBody(
    @SerializedName("message")
    val message: String,
    @SerializedName("fields")
    val fields: Fields?
) {
    data class Fields(
        @SerializedName("email")
        val email: List<String>?
    )
}