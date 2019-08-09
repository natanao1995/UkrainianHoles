package com.example.ukrainianholes.feature.home

data class LastWin(
    val id: Long,
    val status: String,
    val address: String,
    val date: String,
    val daysFromOpen: String,
    val url: String
)