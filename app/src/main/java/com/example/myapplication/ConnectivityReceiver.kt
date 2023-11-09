package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import com.example.myapplication.utils.Common

class ConnectivityReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("TAG", "Connectivity Receiver is ready to use!")
        if(context != null)
            Common.isConnectedToInternet(context)
    }

}