package com.wolfpackdigital.cashli.presentation.main

import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.wolfpackdigital.cashli.ActMainBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseActivity
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import com.wolfpackdigital.cashli.shared.utils.extensions.inflateNewGraph
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActMainBinding, MainActivityViewModel>(R.layout.activity_main) {
    override val viewModel by viewModel<MainActivityViewModel>()

    private var keepSplash = true

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment
    }

    private val navController: NavController by lazy {
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition { keepSplash }
        super.onCreate(savedInstanceState)
    }

    override fun setupViews() {
        setupBottomNavigationView()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.isUserLogged.observe(this) {
            if (it == true)
                navHostFragment.inflateNewGraph(
                    graphId = R.navigation.auth_graph,
                    startDestinationId = R.id.home_graph
                )
        }
        viewModel.keepShowingSplash.observe(this) {
            keepSplash = it
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(viewModel.destinationChangeListener)
    }

    private fun setupBottomNavigationView() {
        binding.mainBottomNav.apply {
            setupWithNavController(navController)
            setOnItemReselectedListener { }
        }
        navController.addOnDestinationChangedListener(viewModel.destinationChangeListener)
    }

    override fun parseNotificationFromIntent(
        notification: NotificationModel?,
        fromBackground: Boolean
    ) {
        when (fromBackground) {
            true -> {
                // TODO handle background notification
            }

            false -> viewModel.handlePushNotificationType(notification)
        }
    }
}
