package com.wolfpackdigital.cashli.shared.notifications

import android.annotation.SuppressLint
import com.wolfpackdigital.cashli.domain.usecases.RegisterDeviceTokenUseCase
import com.wolfpackdigital.cashli.shared.base.BaseMessagingService
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import org.koin.android.ext.android.inject

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessagingService : BaseMessagingService(), PersistenceService {

    private val registerDeviceTokenUseCase by inject<RegisterDeviceTokenUseCase>()

    override suspend fun handleNewToken() {
        userProfile?.let {
            registerDeviceTokenUseCase(Unit)
        }
    }
}
