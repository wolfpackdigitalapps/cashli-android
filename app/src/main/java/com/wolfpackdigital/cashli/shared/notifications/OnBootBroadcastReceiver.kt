package com.wolfpackdigital.cashli.shared.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

private const val MESSAGING_SERVICE_PACKAGE_PREFIX = "data.notifications.MessagingService"

class OnBootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_BOOT_COMPLETED -> context?.packageName?.let { packageName ->
                Intent("$packageName.$MESSAGING_SERVICE_PACKAGE_PREFIX").apply {
                    setClass(context, MessagingService::class.java)
                    context.startService(this)
                }
            }
            else -> {
            }
        }
    }
}
