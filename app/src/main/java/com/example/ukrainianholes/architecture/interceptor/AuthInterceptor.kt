package com.example.ukrainianholes.architecture.interceptor

import com.example.ukrainianholes.data.User
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val user: User
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val rawRequest = chain.request()

        if (rawRequest.method().equals("post", true) &&
            (rawRequest.url().encodedPath().equals("/users", true) ||
                    rawRequest.url().encodedPath().equals("/auth", true))
        ) {
            return chain.proceed(rawRequest)
        }

        val token = user.token

        return if (token == null) {
            chain.proceed(rawRequest)
        } else {
            val request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer ${user.token}")
                .build()
            chain.proceed(request)
        }
    }
}