package com.wolfpackdigital.cashli.presentation.main

import com.wolfpackdigital.cashli.ActMainBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseActivity
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActMainBinding, MainActivityViewModel>(R.layout.activity_main) {
    override val viewModel by viewModel<MainActivityViewModel>()

    override fun setupViews() {
    }

    override fun parseNotificationFromIntent(notification: NotificationModel?) {
        // TODO add routes
    }
}
