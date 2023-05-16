package com.wolfpackdigital.cashli.presentation.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wolfpackdigital.cashli.HomeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
import com.wolfpackdigital.cashli.shared.utils.extensions.areDeviceNotificationsFullyEnabled
import com.wolfpackdigital.cashli.shared.utils.extensions.canScrollBothDirections
import com.wolfpackdigital.cashli.shared.utils.extensions.getBackStackData
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewBottom
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewTop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val SCROLL_TO_TOP = 0

class HomeFragment :
    BaseFragment<HomeBinding, HomeViewModel>(R.layout.fr_home) {

    override val viewModel by viewModel<HomeViewModel>()

    private val bankShortDetailsSectionAdapter: BankShortDetailsSectionAdapter by lazy {
        BankShortDetailsSectionAdapter()
    }
    private val cashAdvanceSectionAdapter: CashAdvanceSectionAdapter by lazy {
        CashAdvanceSectionAdapter()
    }
    private val bankAccountSectionAdapter: BankAccountSectionAdapter by lazy {
        BankAccountSectionAdapter()
    }
    private val bankTransactionsAdapter: BankTransactionsAdapter by lazy {
        BankTransactionsAdapter()
    }
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(
            bankAccountSectionAdapter,
            cashAdvanceSectionAdapter,
            bankShortDetailsSectionAdapter,
            bankTransactionsAdapter
        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            viewModel.handleUserPushNotificationsSetting(isGranted)
        }

    override fun setupViews() {
        binding?.rvHome?.apply {
            adapter = concatAdapter
            itemAnimator = null
        }
        binding?.fabScrollToTop?.setOnClickDebounced {
            binding?.rvHome?.smoothScrollToPosition(SCROLL_TO_TOP)
        }
        handleTransactionsListScroll()
        setupObservers()
    }

    private fun handleTransactionsListScroll() {
        binding?.rvHome?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (bankTransactionsAdapter.itemCount > 0 && binding?.fabScrollToTop?.isShown == false &&
                    (binding?.rvHome?.canScrollBothDirections() == true || binding?.rvHome?.reachedViewBottom() == true)
                ) {
                    binding?.fabScrollToTop?.show()
                } else if (binding?.rvHome?.reachedViewTop() == true &&
                    binding?.fabScrollToTop?.isShown == true
                ) {
                    binding?.fabScrollToTop?.hide()
                }
            }
        })
    }

    private fun setupObservers() {
        viewModel.linkBankAccountInfo.observe(viewLifecycleOwner) { linkBankAccountInfo ->
            bankAccountSectionAdapter.submitList(listOf(linkBankAccountInfo))
            linkBankAccountInfo.bankAccount?.let { bankAccount ->
                lifecycleScope.launch {
                    viewModel.bankTransactionsFlow.collectLatest { pagingData ->
                        bankTransactionsAdapter.submitData(pagingData)
                        handleExtraPaddingForFloatingButton()
                    }
                }
                bankShortDetailsSectionAdapter.submitList(listOf(bankAccount))
            }
        }
        viewModel.requestCashAdvanceInfo.observe(viewLifecycleOwner) {
            cashAdvanceSectionAdapter.submitList(listOf(it))
        }
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                HomeViewModel.Command.CheckPushNotificationPermissions ->
                    handlePushNotificationPermissions()
            }
        }
        setupBackStackData()
    }

    private fun handleExtraPaddingForFloatingButton() {
        val bottomPadding = resources.getDimension(R.dimen.dimen_100dp).toInt()
        binding?.rvHome?.setPadding(0, 0, 0, bottomPadding)
    }

    private fun setupBackStackData() {
        navController?.getBackStackData<Boolean>(
            Constants.REFRESH_USER_DATA,
            viewLifecycleOwner,
            removeValue = true
        ) {
            if (it == true) viewModel.getUserProfile()
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
