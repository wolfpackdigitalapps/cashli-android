@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.LogOutUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.PauseUserAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.args.DeleteAccountArgs
import com.wolfpackdigital.cashli.presentation.entities.enums.MenuItem
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants.SIGN_IN_SCREEN_DL
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class MoreViewModel(
    private val logOutUserUseCase: LogOutUserUseCase,
    private val pauseUserAccountUseCase: PauseUserAccountUseCase,
    private val closeUserAccountUseCase: CloseUserAccountUseCase,
    private val getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase
) : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

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
                // TODO new card
            }

            MenuItem.LOGOUT -> {
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleIdOrString = R.string.log_out,
                        imageId = R.drawable.ic_log_out,
                        contentIdOrString = R.string.log_out_message,
                        buttonPrimaryId = R.string.yes_log_out,
                        isCloseVisible = false,
                        buttonSecondaryId = R.string.cancel,
                        buttonPrimaryClick = { handleLogOut() },
                    )
                )
            }
        }
    }

    private fun showPauseAccountDialog() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.pause_or_close_account,
                imageId = R.drawable.ic_stop,
                contentIdOrString = R.string.pause_account_content,
                buttonPrimaryEnabled = userProfile?.bankAccountConnected ?: false,
                buttonPrimaryId = R.string.pause_account,
                buttonSecondaryId = R.string.close_account_instead,
                buttonPrimaryClick = { handlePauseAccount() },
                buttonSecondaryClick = { handleCloseAccountAllowance() }
            )
        )
    }

    private fun handleCloseAccountAllowance() {
        performApiCall {
            val result = getUserOutstandingBalanceStatusUseCase(Unit)
            result.onSuccess { userOutstandingBalanceStatus ->
                if (userOutstandingBalanceStatus.outstandingBalance)
                    showCloseAccountIneligibleDialog()
                else
                    showCloseAccountDialog()
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun showCloseAccountDialog() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            MoreFragmentDirections.actionMoreFragmentToDeleteAccountDialog(
                DeleteAccountArgs(
                    onDeleteAccount = { reason ->
                        handleCloseAccount(reason)
                    }
                )
            )
        )
    }

    private fun showCloseAccountIneligibleDialog() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.close_account,
                imageId = R.drawable.ic_stop_gray,
                contentIdOrString = R.string.close_account_ineligible,
                buttonPrimaryId = R.string.close_account,
                buttonSecondaryId = R.string.pause_account_instead,
                buttonPrimaryEnabled = false,
                buttonSecondaryClick = { showPauseAccountDialog() }
            )
        )
    }

    private fun handleCloseAccount(reason: String?) {
        performApiCall {
            val result = closeUserAccountUseCase(
                CloseUserAccountReasonRequest(reason = reason)
            )
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

    private fun handlePauseAccount() {
        performApiCall {
            val result = pauseUserAccountUseCase(Unit)
            result.onSuccess {
                userProfile = userProfile?.copy(accountStatus = AccountStatus.PAUSED)
                _menuItems.value = buildList {
                    addAll(menuItems.value ?: emptyList())
                    add(
                        menuItems.value?.indexOf(MenuItem.PAUSE_CLOSE_ACCOUNT) ?: 0,
                        MenuItem.UNPAUSE_CLOSE_ACCOUNT
                    )
                    remove(MenuItem.PAUSE_CLOSE_ACCOUNT)
                }
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
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

    private fun clearDataAndRedirectToLogin() {
        clearUserData()
        _baseCmd.value = BaseCommand.PerformNavDeepLink(
            deepLink = SIGN_IN_SCREEN_DL,
            popUpTo = R.id.navigation
        )
    }

    sealed class Command
}
