package com.example.ukrainianholes.feature.home

import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.remote.ApiService
import com.example.ukrainianholes.data.remote.entity.GetStatsResponse

class HomeInteractor(
    private val apiService: ApiService
) : BaseInteractor() {
    suspend fun getStats(): Result<GetStatsResponse> {
        return processRequest {
            apiService.getStats()
        }
    }
}