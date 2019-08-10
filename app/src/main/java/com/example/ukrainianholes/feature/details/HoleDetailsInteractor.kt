package com.example.ukrainianholes.feature.details

import com.example.ukrainianholes.architecture.base.BaseInteractor
import com.example.ukrainianholes.architecture.base.Result
import com.example.ukrainianholes.architecture.base.mapTo
import com.example.ukrainianholes.data.remote.ApiService

class HoleDetailsInteractor(
    private val apiService: ApiService
) : BaseInteractor() {

    suspend fun setLike(holeId: Long): Result<Boolean> =
        processRequest {
            apiService.setLike(holeId)
        }.mapTo { true }

    suspend fun unsetLike(holeId: Long): Result<Boolean> =
        processRequest {
            apiService.unsetLike(holeId)
        }.mapTo { false }

    suspend fun changeAccidentRate(holeId: Long, rate: Int): Result<Unit> =
        processRequest {
            apiService.changeAccidentRate(holeId, rate)
        }.mapTo { }
}