package com.wolfpackdigital.baseproject.shared.notifications

import android.annotation.SuppressLint
import com.wolfpackdigital.baseproject.shared.base.BaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : BaseMessagingService() {

    override fun handleNewToken() {
        // TODO register token on BE
    }
}
