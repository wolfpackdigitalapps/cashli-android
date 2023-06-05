package com.wolfpackdigital.cashli.presentation.shared

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.PauseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnpauseAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.args.DeleteAccountArgs
import com.wolfpackdigital.cashli.presentation.more.MoreFragmentDirections
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

@Suppress("TooManyFunctions")
abstract class PauseOrCloseAccountViewModel(
    private val pauseUserAccountUseCase: PauseUserAccountUseCase,
    private val closeUserAccountUseCase: CloseUserAccountUseCase,
    private val getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase,
    private val unpauseAccountUseCase: UnpauseAccountUseCase
) : BaseViewModel(), PersistenceService {

    protected fun showPauseAccountDialog() {
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

    protected fun showUnpauseAccountDialog() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.welcome_back,
                imageId = R.drawable.ic_pause,
                contentIdOrString = R.string.unpause_account_description,
                buttonPrimaryId = R.string.unpause_account,
                isCloseVisible = true,
                buttonSecondaryId = R.string.close_account_instead,
                buttonPrimaryClick = ::unpauseAccount,
                buttonSecondaryClick = ::handleCloseAccountAllowance
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
                buttonSecondaryClick = ::showPauseAccountDialog
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

    protected fun clearDataAndRedirectToLogin() {
        clearUserData()
        _baseCmd.value = BaseCommand.PerformNavDeepLink(
            deepLink = Constants.SIGN_IN_SCREEN_DL,
            popUpTo = R.id.navigation
        )
    }
}
