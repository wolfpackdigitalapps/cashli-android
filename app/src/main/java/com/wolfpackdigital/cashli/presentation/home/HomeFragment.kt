@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.presentation.home

import android.Manifest
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.HomeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.LinkBankAccountInfo
import com.wolfpackdigital.cashli.presentation.entities.RequestCashAdvanceInfo
import com.wolfpackdigital.cashli.presentation.main.MainActivityViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.SCROLL_TO_TOP
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
import com.wolfpackdigital.cashli.shared.utils.extensions.areDeviceNotificationsFullyEnabled
import com.wolfpackdigital.cashli.shared.utils.extensions.canScrollBothDirections
import com.wolfpackdigital.cashli.shared.utils.extensions.getBackStackData
import com.wolfpackdigital.cashli.shared.utils.extensions.handleExtraPadding
import com.wolfpackdigital.cashli.shared.utils.extensions.handleNotificationsRequest
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewBottom
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewTop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<HomeBinding, HomeViewModel>(R.layout.fr_home) {

    private val activityViewModel by activityViewModel<MainActivityViewModel>()

    override val viewModel by viewModel<HomeViewModel>()

    private val warningAdapter: GenericWarningAdapter by lazy {
        GenericWarningAdapter()
    }
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
            warningAdapter,
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

    private val updateLinkAccountToPLaidLauncher =
        registerForActivityResult(OpenPlaidLink()) { linkResult ->
            when (linkResult) {
                is LinkSuccess -> {
                    viewModel.onSuccessUpdatingLinkBankAccount(linkResult)
                }

                is LinkExit -> {
                    viewModel.onFailUpdatingLinkBankAccount(linkResult)
                }
            }
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

    @Suppress("ComplexCondition")
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
        activityViewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                MainActivityViewModel.Command.RefreshUserProfileData ->
                    viewModel.getUserProfile()
            }
        }
        viewModel.accountWarnings.observe(viewLifecycleOwner, warningAdapter::submitList)
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                HomeViewModel.Command.CheckPushNotificationPermissions ->
                    handlePushNotificationPermissions()

                is HomeViewModel.Command.RefreshLinkBankAccountInfo ->
                    handleBankAccountInfoSections(it.linkBankAccountInfo)

                is HomeViewModel.Command.RefreshRequestCashAdvanceInfo ->
                    handleRequestCashSection(it.requestCashAdvanceInfo)

                is HomeViewModel.Command.StartUpdatingLinkBankAccount -> {
                    updateLinkAccountToPLaidLauncher.launch(it.linkTokenConfiguration)
                }
            }
        }
        setupBackStackData()
    }

    private fun handleBankAccountInfoSections(linkBankAccountInfo: LinkBankAccountInfo?) {
        linkBankAccountInfo?.let { bankAccountInfo ->
            bankAccountSectionAdapter.submitList(listOf(bankAccountInfo))
            bankAccountInfo.bankAccount?.let { bankAccount ->
                lifecycleScope.launch {
                    viewModel.bankTransactionsFlow.collectLatest { pagingData ->
                        binding?.rvHome?.handleExtraPadding(
                            extraPaddingResId = R.dimen.dimen_100dp
                        )
                        bankTransactionsAdapter.submitData(pagingData)
                    }
                }
                bankShortDetailsSectionAdapter.submitList(listOf(bankAccount))
            } ?: run {
                handleBankInfoNotAvailable()
            }
        } ?: run {
            bankAccountSectionAdapter.submitList(emptyList())
            handleBankInfoNotAvailable()
        }
    }

    private fun handleRequestCashSection(requestCashAdvanceInfo: RequestCashAdvanceInfo?) {
        cashAdvanceSectionAdapter.submitList(
            requestCashAdvanceInfo?.let { item -> listOf(item) } ?: emptyList()
        )
    }

    private fun handleBankInfoNotAvailable() {
        bankShortDetailsSectionAdapter.submitList(emptyList())
        lifecycleScope.launch {
            bankTransactionsAdapter.submitData(PagingData.empty())
        }
        binding?.rvHome?.handleExtraPadding(
            extraPaddingNeeded = false,
            defaultPaddingResId = R.dimen.dimen_20dp
        )
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
        viewModel.getUserProfile()
    }

    override fun onStop() {
        super.onStop()
        viewModel.cancelCheckEligibilityStatusJob()
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
                requestPermissionLauncher,
                onPermissionAlreadyGranted = {
                    val deviceNotificationsEnabled =
                        NotificationManagerCompat.from(ctx).areDeviceNotificationsFullyEnabled()
                    viewModel.handleUserPushNotificationsSetting(deviceNotificationsEnabled)
                }
            )
        }
    }
}
