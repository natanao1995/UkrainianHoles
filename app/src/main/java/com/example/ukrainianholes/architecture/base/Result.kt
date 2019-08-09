package com.example.ukrainianholes.architecture.base

sealed class Result<T>

data class ResultSuccess<T>(
    val data: T
) : Result<T>()

data class ResultError<T>(
    val exception: Exception? = null
) : Result<T>()

class ResultLoading<T>(
    val data: T? = null,
    val progress: Float? = null
) : Result<T>()

inline fun <T, R> Result<T>.mapTo(transform: (T) -> R): Result<R> {
    return when (this) {
        is ResultSuccess -> {
            ResultSuccess(transform(this.data))
        }
        is ResultError -> {
            ResultError(this.exception)
        }
        is ResultLoading -> {
            var newData: R? = null
            if (this.data != null) {
                newData = transform(this.data)
            }
            ResultLoading(newData, this.progress)
        }
    }
}