package com.wolfpackdigital.cashli.application

import android.app.Application
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
