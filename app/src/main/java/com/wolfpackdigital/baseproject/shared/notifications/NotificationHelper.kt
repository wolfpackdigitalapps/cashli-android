package com.wolfpackdigital.baseproject.shared.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationHelper {

    @Suppress("LongParameterList")
    fun createNotificationChannel(
        context: Context,
        importance: Int,
        showBadge: Boolean = false,
        channelId: String,
        name: String,
        description: String
    ) {
        val channel = NotificationChannel(channelId, name, importance).apply {
            this.description = description
            setShowBadge(showBadge)
        }
        context.getSystemService(NotificationManager::class.java)?.apply {
            createNotificationChannel(channel)
        }
    }
}
