package com.wolfpackdigital.cashli.presentation.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetEligibilityStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnlinkAccountUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.plaid.LinkPlaidAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class AccountViewModel(
    generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    completeLinkingBankAccountUseCase: CompleteLinkingBankAccountUseCase,
    getEligibilityStatusUseCase: GetEligibilityStatusUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val unlinkAccountUseCase: UnlinkAccountUseCase
) : LinkPlaidAccountViewModel(
    generateLinkTokenUseCase,
    completeLinkingBankAccountUseCase,
    getEligibilityStatusUseCase
),
    PersistenceService {

    private val _account = MutableLiveData(userProfile?.bankAccount)
    val account: LiveData<BankAccount?> = _account

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
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleId = R.string.account_remove_bank_account,
                imageId = R.drawable.ic_popup_delete,
                contentIdOrString = R.string.account_remove_bank_account_description,
                buttonPrimaryId = R.string.account_remove_bank_account_proceed,
                buttonPrimaryClick = ::deleteAccount,
                buttonSecondaryId = R.string.cancel
            )
        )
    }

    fun onDeleteFailed() {
        _baseCmd.value =
            BaseCommand.ShowPopupById(
                PopupConfig(
                    titleId = R.string.account_remove_bank_account,
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
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }
}
