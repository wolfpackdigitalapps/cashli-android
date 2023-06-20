package com.wolfpackdigital.cashli.application

import android.app.Application
import android.webkit.WebView
import androidx.core.app.NotificationManagerCompat
import com.orhanobut.hawk.Hawk
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.shared.notifications.NotificationHelper
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import com.yariksoffice.lingver.Lingver
import org.koin.core.context.startKoin

@Suppress("unused")
class ApplicationClass : Application(), PersistenceService {
    override fun onCreate() {
        super.onCreate()
        Hawk.init(applicationContext).build()
        startKoin {
            modules(AppModules.modules)
        }

        /**
         This line fixes a bug related to the app locale - without it after a WebView is
         displayed the app locale is changed to the system default
         */
        WebView(applicationContext)

        Lingver.init(this, language?.toString() ?: Language.ENGLISH.toString())
        createNotificationChannel()
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
