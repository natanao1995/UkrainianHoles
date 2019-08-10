package com.example.ukrainianholes.data.remote

import com.example.ukrainianholes.data.remote.entity.AddHoleRequest
import com.example.ukrainianholes.data.remote.entity.FileResponse
import com.example.ukrainianholes.data.remote.entity.Filter.MY
import com.example.ukrainianholes.data.remote.entity.GetStatsResponse
import com.example.ukrainianholes.data.remote.entity.HoleResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @Multipart
    @POST("file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): Response<FileResponse>

    @GET("stats")
    suspend fun getStats(): Response<GetStatsResponse>

    @GET("yamas")
    suspend fun getAllHoles(
        @Query("filter") filter: String = MY,
        @Query("lat") lat: Double?,
        @Query("lng") lng: Double?
    ): Response<List<HoleResponse>>

    @POST("yamas")
    suspend fun addHole(
        @Body body: AddHoleRequest
    ): Response<HoleResponse>
}