package com.wolfpackdigital.cashli.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetEligibilityStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnlinkAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.plaid.LinkPlaidAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDateTime
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class AccountViewModel(
    generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    completeLinkingBankAccountUseCase: CompleteLinkingBankAccountUseCase,
    getEligibilityStatusUseCase: GetEligibilityStatusUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val unlinkAccountUseCase: UnlinkAccountUseCase,
    private val getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase
) : LinkPlaidAccountViewModel(
    generateLinkTokenUseCase,
    completeLinkingBankAccountUseCase,
    getEligibilityStatusUseCase
),
    PersistenceService {

    private val _account = MutableLiveData(userProfile?.bankAccount)
    val account: LiveData<BankAccount?> = _account

    private val _relinkDate =
        MutableLiveData(userProfile?.bankAccount?.relinkableAt?.toFormattedLocalDateTime())
    val relinkDate: LiveData<String?> = _relinkDate

    private var userHasOutstandingBalance = false

    init {
        getOutstandingBalanceStatus()
    }

    private fun getOutstandingBalanceStatus() {
        performApiCall {
            val result = getUserOutstandingBalanceStatusUseCase(Unit)
            result.onSuccess {
                userHasOutstandingBalance = it.outstandingBalance
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun getUserProfile() {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
                _account.value = userProfile?.bankAccount
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun onDeleteAccount() {
        if (userHasOutstandingBalance) {
            displayOutstandingBalanceDialog()
        } else {
            displayDeleteAccountDialog()
        }
    }

    private fun displayDeleteAccountDialog() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.account_remove_bank_account,
                imageId = R.drawable.ic_popup_delete,
                contentIdOrString = R.string.account_remove_bank_account_description,
                buttonPrimaryId = R.string.account_remove_bank_account_proceed,
                buttonPrimaryClick = ::deleteAccount,
                buttonSecondaryId = R.string.cancel
            )
        )
    }

    private fun displayOutstandingBalanceDialog() {
        _baseCmd.value =
            BaseCommand.ShowPopupById(
                PopupConfig(
                    titleIdOrString = R.string.account_remove_bank_account,
                    imageId = R.drawable.ic_popup_delete,
                    contentIdOrString = R.string.account_remove_bank_account_outstanding_advance_balance,
                    secondaryContent = R.string.account_remove_bank_account_try_again,
                    isSecondaryContentBold = true
                )
            )
    }

    private fun deleteAccount() {
        performApiCall {
            with(unlinkAccountUseCase(Unit)) {
                onSuccess {
                    getUserProfile()
                }
                onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.message ?: R.string.generic_error
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    @Suppress("MagicNumber")
    override fun onEligibleStatusCallback() {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = R.string.congrats,
                contentIdOrString = R.string.bank_account_connection_success,
                contentFormatArgs = arrayOf(123),
                imageId = R.drawable.ic_congrats,
                isCloseVisible = true,
                buttonPrimaryId = R.string.cash_out,
                buttonPrimaryClick = {
                    _baseCmd.value = BaseCommand.PerformNavAction(
                        AccountFragmentDirections.actionAccountFragmentToClaimCashGraph(),
                        popUpTo = R.id.accountFragment,
                        inclusive = true
                    )
                }
            )
        )
    }

    override fun onNotEligibleStatusCallback() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            AccountFragmentDirections.actionAccountFragmentToIneligibleGraph(),
            popUpTo = R.id.accountFragment,
            inclusive = true
        )
    }

    override fun onEligibilityStatusCheckError() {
        return
    }

    override fun handlePlaidErrorPopup(titleId: Int, contentIdOrString: Any?) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = titleId,
                contentIdOrString = contentIdOrString,
                imageId = R.drawable.ic_warning,
                isCloseVisible = true,
                buttonSecondaryId = R.string.get_support,
                buttonSecondaryClick = {
                    _baseCmd.value = BaseCommand.OpenSMSApp()
                },
            )
        )
    }
}
