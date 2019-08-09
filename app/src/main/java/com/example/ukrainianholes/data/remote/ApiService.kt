package com.example.ukrainianholes.data.remote

import com.example.ukrainianholes.data.remote.entity.FileResponse
import com.example.ukrainianholes.data.remote.entity.GetStatsResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
    @Multipart
    @POST("file")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part
    ): Response<FileResponse>

    @GET("stats")
    suspend fun getStats(): Response<GetStatsResponse>
}