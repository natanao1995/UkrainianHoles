package com.example.ukrainianholes.feature.all_holes

import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.remote.ApiService
import com.example.ukrainianholes.data.remote.entity.HoleResponse

class AllHolesInteractor(
    private val apiService: ApiService
) : BaseInteractor() {
    suspend fun getAllHoles(filter: String, lat: Double? = null, lng: Double? = null): Result<List<HoleResponse>> {
        return processRequest {
            apiService.getAllHoles(filter, lat, lng)
        }
    }
}