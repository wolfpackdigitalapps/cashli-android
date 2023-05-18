package com.wolfpackdigital.cashli.presentation.auth.signup.validateCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByIdentifierUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseValidateCodeViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants.STEP_1
import com.wolfpackdigital.cashli.shared.utils.Constants.STEP_2
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet

class ValidateCodeViewModel(
    private val identifier: String?,
    private val codeReceivedViaType: CodeReceivedViaType,
    private val validateCodeByIdentifierUseCase: ValidateCodeByIdentifierUseCase,
    private val submitRegistrationIdentifiersUseCase: SubmitRegistrationIdentifiersUseCase
) : BaseValidateCodeViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = when (codeReceivedViaType) {
                CodeReceivedViaType.SMS -> STEP_1
                CodeReceivedViaType.EMAIL -> STEP_2
            },
            isStepCounterVisible = true,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val titleRes: Int by lazy {
        when (codeReceivedViaType) {
            CodeReceivedViaType.SMS -> R.string.code_from_sms
            CodeReceivedViaType.EMAIL -> R.string.code_from_email
        }
    }

    init {
        initResendCode()
    }

    fun getSupport() {
        _cmd.value = Command.GetSupport
    }

    fun onContinueClicked() {
        safeLet(identifier, verificationCode.value) { identifier, verificationCode ->
            performApiCall {
                val request = IdentifiersCodeValidationRequest(
                    identifier = identifier,
                    code = verificationCode
                )
                validateCode(
                    request,
                    validateCodeByIdentifierUseCase,
                    onSuccessAction = { identifierToken ->
                        onValidateCodeSuccess(identifierToken)
                    }
                )
            }
        }
    }

    private fun onValidateCodeSuccess(identifierToken: IdentifierToken) {
        when (codeReceivedViaType) {
            CodeReceivedViaType.SMS -> {
                _cmd.value = Command.SavePhoneNumberToken(identifierToken.token)
                _baseCmd.value = BaseCommand.PerformNavAction(
                    ValidateCodeFragmentDirections.actionValidateCodeFragmentToCreateProfileFragment()
                )
            }
            CodeReceivedViaType.EMAIL -> {
                _cmd.value = Command.SaveEmailToken(identifierToken.token)
                _baseCmd.value = BaseCommand.PerformNavAction(
                    ValidateCodeFragmentDirections.actionValidateCodeFragmentToChoosePasswordFragment()
                )
            }
        }
    }

    fun onResendConfirmationCodeClicked() {
        resendConfirmationCodeAndStartTimer(
            identifier,
            codeReceivedViaType,
            submitRegistrationIdentifiersUseCase
        )
    }

    sealed class Command {
        object GetSupport : Command()
        data class SavePhoneNumberToken(val phoneNumberToken: String) : Command()
        data class SaveEmailToken(val emailToken: String) : Command()
    }
}
