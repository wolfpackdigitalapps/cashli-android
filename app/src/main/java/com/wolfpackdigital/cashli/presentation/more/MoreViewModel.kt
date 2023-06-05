@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.LogOutUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.PauseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnpauseAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.enums.MenuItem
import com.wolfpackdigital.cashli.presentation.shared.PauseOrCloseAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess

class MoreViewModel(
    private val logOutUserUseCase: LogOutUserUseCase,
    pauseUserAccountUseCase: PauseUserAccountUseCase,
    closeUserAccountUseCase: CloseUserAccountUseCase,
    getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase: UnpauseAccountUseCase
) : PauseOrCloseAccountViewModel(
    pauseUserAccountUseCase,
    closeUserAccountUseCase,
    getUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase
) {

    private val _firstName = MutableLiveData(userProfile?.firstName)
    val firstName: LiveData<String?> = _firstName
    private val _lastName = MutableLiveData(userProfile?.lastName)
    val lastName: LiveData<String?> = _lastName

    private val _menuItems = MutableLiveData<List<MenuItem>>()
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    fun handleMenuItems() {
        _menuItems.value = buildList {
            addAll(MenuItem.values())
            when (userProfile?.accountStatus) {
                AccountStatus.PAUSED -> remove(MenuItem.PAUSE_CLOSE_ACCOUNT)
                else -> remove(MenuItem.UNPAUSE_CLOSE_ACCOUNT)
            }
            if (userProfile?.lastMembership == null)
                remove(MenuItem.MEMBERSHIP_ADVANCE_HISTORY)
        }
    }

    fun handleOnMenuItemClicked(menuItem: MenuItem) {
        when (menuItem) {
            MenuItem.MEMBERSHIP_ADVANCE_HISTORY -> {
                _baseCmd.value = BaseCommand.PerformNavAction(
                    MoreFragmentDirections.actionMoreFragmentToMembershipFragment()
                )
            }

            MenuItem.TERMS_AND_CONDITIONS -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.terms_and_conditions_url)
            }

            MenuItem.PRIVACY_POLICY -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.privacy_policy_url)
            }

            MenuItem.SETTINGS -> {
                _baseCmd.value = BaseCommand.PerformNavAction(
                    MoreFragmentDirections.actionMoreFragmentToSettingsFragment()
                )
            }

            MenuItem.HELP -> {
                _baseCmd.value = BaseCommand.PerformNavAction(
                    MoreFragmentDirections.actionMoreFragmentToHelpFragment()
                )
            }

            MenuItem.REFER_FRIEND -> {
                // TODO new card
            }

            MenuItem.FAQ -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.faq_url)
            }

            MenuItem.PAUSE_CLOSE_ACCOUNT -> {
                showPauseAccountDialog()
            }

            MenuItem.UNPAUSE_CLOSE_ACCOUNT -> {
                showUnpauseAccountDialog()
            }

            MenuItem.LOGOUT -> {
                showLogoutDialog()
            }
        }
    }

    private fun showLogoutDialog() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.log_out,
                imageId = R.drawable.ic_log_out,
                contentIdOrString = R.string.log_out_message,
                buttonPrimaryId = R.string.yes_log_out,
                isCloseVisible = false,
                buttonSecondaryId = R.string.cancel,
                buttonPrimaryClick = ::handleLogOut,
            )
        )
    }

    override fun onPausedAccountSuccessful() {
        handleMenuItems()
    }

    fun goToEditProfileScreen() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            MoreFragmentDirections.actionMoreFragmentToEditProfileFragment()
        )
    }

    fun setUpdatedData() {
        _firstName.value = userProfile?.firstName
        _lastName.value = userProfile?.lastName
    }

    private fun handleLogOut() {
        performApiCall {
            val result = logOutUserUseCase(Unit)
            result.onSuccess {
                clearDataAndRedirectToLogin()
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }
}
