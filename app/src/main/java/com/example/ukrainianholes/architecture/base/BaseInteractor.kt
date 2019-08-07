package com.example.ukrainianholes.architecture.base

import com.example.ukrainianholes.data.remote.entity.ErrorBody
import com.google.gson.Gson
import retrofit2.Response

open class BaseInteractor {
    suspend fun <T : Any> processRequest(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call.invoke()

            if (response.isSuccessful)
                ResultSuccess(response.body()!!)
            else {
                handleErrorResponse(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            ResultError(e)
        }
    }

    private fun <T : Any> handleErrorResponse(responseString: String?): ResultError<T> {
        val errorEntity = Gson().fromJson(responseString, ErrorBody::class.java) ?: return ResultError(Exception())
        var message = errorEntity.message
        errorEntity.fields?.email?.let {
            message += "\n" + it
        }
        return ResultError(exception = Exception(message))
    }
}