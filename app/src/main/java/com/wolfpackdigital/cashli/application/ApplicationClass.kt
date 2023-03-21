package com.wolfpackdigital.cashli.application

import android.app.Application
import androidx.core.app.NotificationManagerCompat
import com.orhanobut.hawk.Hawk
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.notifications.NotificationHelper
import org.koin.core.context.startKoin

@Suppress("unused")
class ApplicationClass : Application() {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
        startKoin {
            modules(AppModules.modules)
        }
        // Uncomment this line if you use Push Notifications
        // createNotificationChannel()
    }

    private fun createNotificationChannel() {
        NotificationHelper.createNotificationChannel(
            this,
            importance = NotificationManagerCompat.IMPORTANCE_HIGH,
            channelId = getString(R.string.push_notification_channel_id),
            name = getString(R.string.push_notification_channel_name),
            description = getString(R.string.push_notification_channel_desc)
        )
    }
}
