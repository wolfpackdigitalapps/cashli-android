package com.wolfpackdigital.cashli.presentation.main

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wolfpackdigital.cashli.ActMainBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseActivity
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActMainBinding, MainActivityViewModel>(R.layout.activity_main) {
    override val viewModel by viewModel<MainActivityViewModel>()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplash }
        super.onCreate(savedInstanceState)
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.keepShowingSplash.observe(this) {
            keepSplash = it
        }
    }

    override fun parseNotificationFromIntent(notification: NotificationModel?) {
        // TODO add routes
    }
}
