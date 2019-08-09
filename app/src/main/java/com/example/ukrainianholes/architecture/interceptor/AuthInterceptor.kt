package com.example.ukrainianholes.architecture.interceptor

import android.content.Context
import android.provider.Settings
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        val request = chain.request()
            .newBuilder()
            .addHeader("device-id", deviceId)
            .build()
        return chain.proceed(request)
    }
}