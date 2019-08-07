package com.example.ukrainianholes.util

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtil {
    fun isNetworkConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val netInfo = cm.activeNetworkInfo ?: return false
        return netInfo.isConnected
    }
}
