package com.example.ukrainianholes.data.remote

import com.example.ukrainianholes.data.remote.entity.FileResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {
//    @GET("tasks")
//    suspend fun getTasks(
//        @Query("page") page: Int? = null,
//        @Query("sort") sort: String? = null
//    ): Response<TasksListBody>
//
@Multipart
@POST("file")
suspend fun uploadFile(
    @Part file: MultipartBody.Part
): Response<FileResponse>
}