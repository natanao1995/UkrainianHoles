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
    val holes: List<HoleResponse>
)