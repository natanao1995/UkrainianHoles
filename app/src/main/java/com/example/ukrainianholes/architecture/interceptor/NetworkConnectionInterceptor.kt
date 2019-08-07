package com.example.ukrainianholes.architecture.interceptor

import android.content.Context
import com.example.ukrainianholes.architecture.exception.NoInternetException
import com.example.ukrainianholes.util.NetworkUtil
import okhttp3.Interceptor
import okhttp3.Response

class NetworkConnectionInterceptor(
    private val context: Context
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!NetworkUtil.isNetworkConnected(context)) {
            throw NoInternetException()
        }

        return chain.proceed(chain.request())
    }
}