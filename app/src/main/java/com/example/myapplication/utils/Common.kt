package com.example.myapplication.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log

class Common {
    companion object{
        fun isConnectedToInternet(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.allNetworkInfo
            for (networkInfo in info) {
                Log.d("TAG", "Is connected: ${networkInfo?.isConnected}")
                Log.d("TAG", "Type: ${networkInfo?.type} ${ConnectivityManager.TYPE_WIFI}")
            }
            return false
        }
    }
}