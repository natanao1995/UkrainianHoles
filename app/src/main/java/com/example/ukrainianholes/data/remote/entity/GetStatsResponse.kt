package com.example.ukrainianholes.data.remote.entity

import com.google.gson.annotations.SerializedName

data class GetStatsResponse(
    @SerializedName("created_count")
    val createdCount: Int,
    @SerializedName("fixed_count")
    val fixedCount: Int,
    @SerializedName("in_progress_count")
    val inProgressCount: Int,
    @SerializedName("last")
    val last: List<Last>
) {
    data class Last(
        @SerializedName("id")
        val id: Long,
        @SerializedName("address")
        val address: String,
        @SerializedName("comment")
        val comment: String,
        @SerializedName("fixed_photos")
        val fixedPhotos: List<FixedPhoto>,
        @SerializedName("inserted_at")
        val insertedAt: String,
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
        val updatedAt: String
    ) {
        data class Photo(
            @SerializedName("id")
            val id: Long
        )

        data class FixedPhoto(
            @SerializedName("id")
            val id: Long
        )
    }
}