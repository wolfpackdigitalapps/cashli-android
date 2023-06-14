package com.wolfpackdigital.cashli.presentation.shared

import androidx.annotation.IdRes
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.PauseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnpauseAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.args.DeleteAccountArgs
import com.wolfpackdigital.cashli.presentation.home.HomeFragmentDirections
import com.wolfpackdigital.cashli.presentation.more.MoreFragmentDirections
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess

@Suppress("TooManyFunctions")
abstract class PauseOrCloseAccountViewModel(
    private val pauseUserAccountUseCase: PauseUserAccountUseCase,
    private val closeUserAccountUseCase: CloseUserAccountUseCase,
    private val getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase,
    private val unpauseAccountUseCase: UnpauseAccountUseCase
) : BaseViewModel() {

    protected fun showPauseAccountDialog(@IdRes fromScreen: Int = R.id.moreFragment) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.pause_or_close_account,
                imageId = R.drawable.ic_stop,
                contentIdOrString = R.string.pause_account_content,
                buttonPrimaryEnabled = userProfile?.bankAccountConnected ?: false,
                buttonPrimaryId = R.string.pause_account,
                buttonSecondaryId = R.string.close_account_instead,
                buttonPrimaryClick = ::handlePauseAccount,
                buttonSecondaryClick = { handleCloseAccountAllowance(fromScreen) }
            )
        )
    }

    protected fun showUnpauseAccountDialog(@IdRes fromScreen: Int = R.id.moreFragment) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.welcome_back,
                imageId = R.drawable.ic_pause,
                contentIdOrString = R.string.unpause_account_description,
                buttonPrimaryId = R.string.unpause_account,
                isCloseVisible = true,
                buttonSecondaryId = R.string.close_account_instead,
                buttonPrimaryClick = ::unpauseAccount,
                buttonSecondaryClick = { handleCloseAccountAllowance(fromScreen) }
            )
        )
    }

    private fun handleCloseAccountAllowance(@IdRes fromScreen: Int = R.id.moreFragment) {
        performApiCall {
            val result = getUserOutstandingBalanceStatusUseCase(Unit)
            result.onSuccess { userOutstandingBalanceStatus ->
                if (userOutstandingBalanceStatus.outstandingBalance)
                    showCloseAccountIneligibleDialog(fromScreen)
                else
                    showCloseAccountDialog(fromScreen)
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun showCloseAccountDialog(@IdRes fromScreen: Int = R.id.moreFragment) {
        val deleteArgs = DeleteAccountArgs(
            onDeleteAccount = { reason ->
                handleCloseAccount(reason)
            }
        )
        _baseCmd.value = when (fromScreen) {
            R.id.homeFragment -> {
                BaseCommand.PerformNavAction(
                    HomeFragmentDirections.actionHomeFragmentToDeleteAccountDialog(deleteArgs)
                )
            }

            R.id.moreFragment -> {
                BaseCommand.PerformNavAction(
                    MoreFragmentDirections.actionMoreFragmentToDeleteAccountDialog(deleteArgs)
                )
            }

            else -> return
        }
    }

    private fun showCloseAccountIneligibleDialog(@IdRes fromScreen: Int = R.id.moreFragment) {
        val accountPaused = userProfile?.accountStatus == AccountStatus.PAUSED
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.close_account,
                imageId = R.drawable.ic_stop_gray,
                contentIdOrString = R.string.close_account_ineligible,
                buttonPrimaryId = R.string.close_account,
                buttonSecondaryId = if (accountPaused) {
                    R.string.unpause_account_instead
                } else {
                    R.string.pause_account_instead
                },
                buttonPrimaryEnabled = false,
                buttonSecondaryClick = if (accountPaused) {
                    { showUnpauseAccountDialog(fromScreen) }
                } else {
                    { showPauseAccountDialog(fromScreen) }
                }
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

    private fun unpauseAccount() {
        performApiCall {
            val result = unpauseAccountUseCase(Unit)
            result.onSuccess {
                onUnpauseAccountSuccessful()
            }
            result.onError {
                val error = it.message ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun handlePauseAccount() {
        performApiCall {
            val result = pauseUserAccountUseCase(Unit)
            result.onSuccess {
                userProfile = userProfile?.copy(accountStatus = AccountStatus.PAUSED)
                onPausedAccountSuccessful()
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    open fun onPausedAccountSuccessful() {}
    open fun onUnpauseAccountSuccessful() {
        _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
    }
}
