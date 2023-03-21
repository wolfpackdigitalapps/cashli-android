package com.wolfpackdigital.cashli.shared.notifications

import android.annotation.SuppressLint
import com.wolfpackdigital.cashli.shared.base.BaseMessagingService

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : BaseMessagingService() {

    override fun handleNewToken() {
        // TODO register token on BE
    }
}
