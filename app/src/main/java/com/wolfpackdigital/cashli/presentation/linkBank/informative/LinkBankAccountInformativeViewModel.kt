package com.wolfpackdigital.cashli.presentation.linkBank.informative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.NavigationDirections
import com.wolfpackdigital.cashli.R
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
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class LinkBankAccountInformativeViewModel(
    private val generateLinkTokenUseCase: GenerateLinkTokenUseCase,
    private val completeLinkingBankAccountUseCase: CompleteLinkingBankAccountUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(onBack = ::back)
    )
    val toolbar: LiveData<Toolbar> = _toolbar

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
//                _baseCmd.value = BaseCommand.ShowPopupById(
//                    PopupConfig(
//                        titleId = R.string.pending,
//                        contentIdOrString = R.string.pending_content,
//                        imageId = R.drawable.ic_pending
//                    )
//                )
                // TODO replace magic number after BE
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleId = R.string.congrats,
                        contentIdOrString = R.string.bank_account_connection_success,
                        contentFormatArgs = arrayOf(123),
                        imageId = R.drawable.ic_congrats,
                        buttonPrimaryId = R.string.cash_out,
                        buttonSecondaryId = R.string.take_me_to_home,
                        buttonSecondaryClick = {
                            _baseCmd.value = BaseCommand.GoBackTo(R.id.homeFragment)
                        },
                        buttonPrimaryClick = {
                            // TODO add redirect to cash out
                        }
                    )
                )
            }
            result.onError {
                // TODO Show different error message TBD
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
            _baseCmd.value = BaseCommand.ShowPopupById(
                PopupConfig(
                    titleId = R.string.connection_failed,
                    contentIdOrString = R.string.bank_account_connection_fail,
                    imageId = R.drawable.ic_warning,
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
    }

    sealed class Command {
        data class StartLinkingBackAccount(
            val linkTokenConfiguration: LinkTokenConfiguration
        ) : Command()
    }
}
