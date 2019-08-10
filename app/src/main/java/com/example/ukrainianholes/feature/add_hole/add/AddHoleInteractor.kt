package com.example.ukrainianholes.feature.add_hole.add

import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.data.remote.ApiService
import com.example.ukrainianholes.data.remote.entity.AddHoleRequest
import com.example.ukrainianholes.data.remote.entity.HoleResponse

class AddHoleInteractor(
    private val apiService: ApiService
) : BaseInteractor() {
    suspend fun addHole(hole: AddHoleRequest): Result<HoleResponse> {
        return processRequest {
            apiService.addHole(hole)
        }
    }
}