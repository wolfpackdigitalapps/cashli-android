package com.wolfpackdigital.cashli.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.wolfpackdigital.cashli.HomeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.areDeviceNotificationsFullyEnabled
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<HomeBinding, HomeViewModel>(R.layout.fr_home) {

    override val viewModel by viewModel<HomeViewModel>()

    private val bankTransactionsAdapter: BankTransactionsAdapter by lazy { BankTransactionsAdapter() }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            viewModel.handleUserPushNotificationsSetting(isGranted)
        }

    override fun setupViews() {
        binding?.rvBankTransactions?.apply {
            adapter = bankTransactionsAdapter
        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                HomeViewModel.Command.CheckPushNotificationPermissions ->
                    handlePushNotificationPermissions()
            }
        }
        lifecycleScope.launch {
            viewModel.bankTransactionsFlow.collectLatest { pagingData ->
                bankTransactionsAdapter.submitData(pagingData)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        // TODO remove mock data
        viewModel.mockRequestCashAdvance()
        viewModel.getUserProfile()
    }

    private fun handlePushNotificationPermissions() {
        context?.let { ctx ->
            if (ContextCompat.checkSelfPermission(
                    ctx,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_DENIED &&
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
            ) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                val deviceNotificationsEnabled =
                    NotificationManagerCompat.from(ctx).areDeviceNotificationsFullyEnabled()
                viewModel.handleUserPushNotificationsSetting(deviceNotificationsEnabled)
            }
        }
    }
}
