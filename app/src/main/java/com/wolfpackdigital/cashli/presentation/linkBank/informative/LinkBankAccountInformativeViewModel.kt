package com.wolfpackdigital.cashli.presentation.linkBank.informative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.usecases.CompleteLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateLinkTokenUseCase
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
            val request = CompleteLinkBankAccountRequest(
                publicToken = linkSuccess.publicToken,
                metadata = linkSuccess.metadata
            )
            val result = completeLinkingBankAccountUseCase(request)
            result.onSuccess {
                // TODO Show success message
            }
            result.onError {
                // TODO Show error message
            }
        }
    }

    fun onFailLinkingBankAccount(linkFail: LinkExit) {
        linkFail.error?.let {
            // TODO Show error message
        }
    }

    sealed class Command {
        data class StartLinkingBackAccount(
            val linkTokenConfiguration: LinkTokenConfiguration
        ) : Command()
    }
}
