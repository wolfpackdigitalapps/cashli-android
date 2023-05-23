package com.wolfpackdigital.cashli.presentation.more.settings

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.SettingsBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.areDeviceNotificationsFullyEnabled
import com.wolfpackdigital.cashli.shared.utils.extensions.handleNotificationsRequest
import com.wolfpackdigital.cashli.shared.utils.extensions.openAppSettings
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsBinding, SettingsViewModel>(R.layout.fr_settings) {

    override val viewModel by viewModel<SettingsViewModel>()

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.POST_NOTIFICATIONS
                else
                    null
            val shouldOpenSettings = permission?.let {
                !isGranted && !shouldShowRequestPermissionRationale(permission)
            } ?: true
            if (shouldOpenSettings)
                checkSystemNotificationsStatus()
        }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when (command) {
                SettingsViewModel.Command.TransitionToStart -> transitionToStart()
                SettingsViewModel.Command.TransitionToEnd -> transitionToEnd()
                SettingsViewModel.Command.CheckPushNotificationPermissions ->
                    handlePushNotificationPermissions()
            }
        }
    }

    private fun checkSystemNotificationsStatus() {
        when {
            NotificationManagerCompat.from(requireContext())
                .areDeviceNotificationsFullyEnabled() -> viewModel.enableNotifications()

            else -> openAppSettings()
        }
    }

    private fun handlePushNotificationPermissions() {
        context?.let { ctx ->
            val permission =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                    Manifest.permission.POST_NOTIFICATIONS
                else
                    null
            ctx.handleNotificationsRequest(
                permission,
                requestNotificationPermissionLauncher,
                onPermissionAlreadyGranted = {
                    checkSystemNotificationsStatus()
                }
            )
        }
    }

    private fun transitionToStart() {
        binding?.mlBalanceAlertAmount?.transitionToStart()
    }

    private fun transitionToEnd() {
        binding?.mlBalanceAlertAmount?.transitionToEnd()
    }
}
