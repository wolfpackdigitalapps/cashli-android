package com.wolfpackdigital.baseproject.presentation.main

import com.wolfpackdigital.baseproject.ActMainBinding
import com.wolfpackdigital.baseproject.R
import com.wolfpackdigital.baseproject.shared.notifications.NotificationModel
import com.wolfpackdigital.baseproject.shared.base.BaseActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActMainBinding, MainActivityViewModel>(R.layout.activity_main) {
    override val viewModel by viewModel<MainActivityViewModel>()

    override fun setupViews() {
    }

    override fun parseNotificationFromIntent(notification: NotificationModel?) {
        // TODO add routes
    }
}
