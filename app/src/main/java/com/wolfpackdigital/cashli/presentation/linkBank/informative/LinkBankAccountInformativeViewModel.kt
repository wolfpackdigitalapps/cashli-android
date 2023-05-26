package com.wolfpackdigital.cashli.presentation.linkBank.informative

import android.app.AlertDialog
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.LinkAccountGraphDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountBalanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountInfoRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountLocalizedBalanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountMetadataRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountSubtypeRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountTypeRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountVerificationStatusRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkInstitutionRequest
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetEligibilityStatusUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.COUNT_DOWN_TIME_30_SEC
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.initTimer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class LinkBankAccountInformativeViewModel(
    private val generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    private val completeLinkingBankAccountUseCase: CompleteLinkingBankAccountUseCase,
    private val getEligibilityStatusUseCase: GetEligibilityStatusUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(onBack = ::back)
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private var checkEligibilityStatusJob: Job? = null

    fun linkMyBankAccount() {
        performApiCall {
            val result = generateLinkTokenUseCase(Unit)
            result.onSuccess { bankToken ->
                val linkTokenConfiguration = linkTokenConfiguration {
                    token = bankToken.linkToken
                }
                _cmd.value = Command.StartLinkingBackAccount(linkTokenConfiguration)
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun onSuccessLinkingBankAccount(linkSuccess: LinkSuccess) {
        performApiCall {
            val request = createLinkBankAccountRequest(linkSuccess)
            val result = completeLinkingBankAccountUseCase(request)
            result.onSuccess {
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleIdOrString = R.string.pending,
                        contentIdOrString = R.string.pending_content,
                        imageId = R.drawable.ic_pending,
                        isCloseVisible = false,
                        otherAction = { alertDialog ->
                            toggleEligibilityStatusJob(alertDialog)
                        },
                        isOtherActionInstant = true
                    )
                )
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                when (it.errorCode) {
                    Constants.ERROR_CODE_409 ->
                        handlePlaidErrorPopup(
                            titleId = R.string.duplicate_account,
                            contentIdOrString = R.string.bank_account_already_in_use
                        )

                    else -> _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    private fun initCheckEligibilityStatusJob(alertDialog: AlertDialog) {
        // TODO replace eligibility delay with minutes after more tests
        checkEligibilityStatusJob = initTimer(COUNT_DOWN_TIME_30_SEC).onCompletion {
            if (it == null)
                handleEligibilityStatus(alertDialog)
            cancelCheckEligibilityStatusJob()
        }.launchIn(viewModelScope)
    }

    private fun toggleEligibilityStatusJob(alertDialog: AlertDialog) {
        viewModelScope.launch {
            checkEligibilityStatusJob?.let {
                cancelCheckEligibilityStatusJob()
            }
            initCheckEligibilityStatusJob(alertDialog)
        }
    }

    private fun cancelCheckEligibilityStatusJob() {
        checkEligibilityStatusJob?.cancel()
        checkEligibilityStatusJob = null
    }

    @Suppress("MagicNumber")
    private fun handleEligibilityStatus(alertDialog: AlertDialog) {
        viewModelScope.launch {
            val result = getEligibilityStatusUseCase(Unit)
            result.onSuccess { eligibilityStatus ->
                when (eligibilityStatus.status) {
                    EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> {
                        toggleEligibilityStatusJob(alertDialog)
                    }

                    EligibilityStatus.ELIGIBLE -> {
                        alertDialog.dismiss()
                        _baseCmd.value = BaseCommand.ShowPopupById(
                            PopupConfig(
                                titleIdOrString = R.string.congrats,
                                contentIdOrString = R.string.bank_account_connection_success,
                                contentFormatArgs = arrayOf(123),
                                imageId = R.drawable.ic_congrats,
                                isCloseVisible = false,
                                buttonPrimaryId = R.string.cash_out,
                                buttonSecondaryId = R.string.take_me_to_home,
                                buttonSecondaryClick = {
                                    _cmd.value = Command.RefreshUserDataNeeded
                                    _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
                                },
                                buttonPrimaryClick = {
                                    _baseCmd.value = BaseCommand.PerformNavAction(
                                        LinkAccountGraphDirections.actionGlobalClaimCashGraph(),
                                        popUpTo = R.id.linkBankAccountInformativeFragment,
                                        inclusive = true
                                    )
                                }
                            )
                        )
                    }

                    EligibilityStatus.NOT_ELIGIBLE -> {
                        alertDialog.dismiss()
                        _baseCmd.value = BaseCommand.PerformNavAction(
                            LinkBankAccountInformativeFragmentDirections
                                .actionLinkBankAccountInformativeFragmentToIneligibleInformativeFragment(),
                            popUpTo = R.id.linkBankAccountInformativeFragment,
                            inclusive = true
                        )
                    }

                    else -> {
                        // TODO add logic if necessary
                    }
                }
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
                alertDialog.dismiss()
                _cmd.value = Command.RefreshUserDataNeeded
                _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
            }
        }
    }

    private fun createLinkBankAccountRequest(linkSuccess: LinkSuccess) =
        CompleteLinkBankAccountRequest(
            publicToken = linkSuccess.publicToken,
            metadata = LinkAccountMetadataRequest(
                accounts = linkSuccess.metadata.accounts.map { linkAccount ->
                    LinkAccountInfoRequest(
                        id = linkAccount.id,
                        name = linkAccount.name,
                        mask = linkAccount.mask,
                        subtype = LinkAccountSubtypeRequest(
                            name = linkAccount.subtype.json,
                            type = LinkAccountTypeRequest(
                                name = linkAccount.subtype.accountType.json
                            )
                        ),
                        verificationStatus = LinkAccountVerificationStatusRequest(
                            name = linkAccount.verificationStatus?.json
                        ),
                        balance = LinkAccountBalanceRequest(
                            available = linkAccount.balance?.available,
                            currency = linkAccount.balance?.currency,
                            current = linkAccount.balance?.current,
                            localized = LinkAccountLocalizedBalanceRequest(
                                available = linkAccount.balance?.localized?.available,
                                current = linkAccount.balance?.localized?.current
                            )
                        )
                    )
                },
                institution = LinkInstitutionRequest(
                    id = linkSuccess.metadata.institution?.id,
                    name = linkSuccess.metadata.institution?.name
                ),
                linkSessionId = linkSuccess.metadata.linkSessionId,
                metadataJson = linkSuccess.metadata.metadataJson
            )
        )

    fun onFailLinkingBankAccount(linkFail: LinkExit) {
        linkFail.error?.let {
            handlePlaidErrorPopup(
                titleId = R.string.connection_failed,
                contentIdOrString = R.string.bank_account_connection_fail
            )
        }
    }

    private fun handlePlaidErrorPopup(@StringRes titleId: Int, contentIdOrString: Any?) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = titleId,
                contentIdOrString = contentIdOrString,
                imageId = R.drawable.ic_warning,
                isCloseVisible = false,
                buttonPrimaryId = R.string.go_back_to_home,
                buttonSecondaryId = R.string.get_support,
                buttonSecondaryClick = {
                    _baseCmd.value = BaseCommand.ShowSMSApp()
                },
                buttonPrimaryClick = {
                    _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
                }
            )
        )
    }

    sealed class Command {
        object RefreshUserDataNeeded : Command()
        data class StartLinkingBackAccount(
            val linkTokenConfiguration: LinkTokenConfiguration
        ) : Command()
    }
}
