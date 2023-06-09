package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.usecases.SubmitChangeIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.SubmitPasswordIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByPasswordIdentifierUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByUpdateIdentifiersUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.ApiError
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseValidateCodeViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.utils.Constants

private const val PHONE_NUMBER_PREFIX = "+1 "

@Suppress("LongParameterList")
class ConfirmOneTimePasswordViewModel(
    private val phoneNumberOrEmail: String,
    private val codeReceivedViaType: CodeReceivedViaType,
    private val fromEditProfile: Boolean,
    private val submitPasswordIdentifiersUseCase: SubmitPasswordIdentifiersUseCase,
    private val validateCodeByPasswordIdentifierUseCase: ValidateCodeByPasswordIdentifierUseCase,
    private val submitChangeIdentifiersUseCase: SubmitChangeIdentifiersUseCase,
    private val validateCodeByUpdateIdentifiersUseCase: ValidateCodeByUpdateIdentifiersUseCase
) : BaseValidateCodeViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.confirm_one_time_password,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val phoneNumberOrEmailString = when (codeReceivedViaType) {
        CodeReceivedViaType.SMS -> "$PHONE_NUMBER_PREFIX$phoneNumberOrEmail"
        CodeReceivedViaType.EMAIL -> phoneNumberOrEmail
    }

    init {
        initResendCode()
    }

    fun onContinueClicked() {
        when (fromEditProfile) {
            true -> {
                onContinueFromEditProfile()
            }

            false -> {
                onContinueFromResetPassword()
            }
        }
    }

    private fun onContinueFromResetPassword() {
        performApiCall {
            verificationCode.value?.let { code ->
                val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                    Constants.PHONE_PREFIX_RO
                else
                    Constants.PHONE_PREFIX_US
                val request = IdentifiersCodeValidationRequest(
                    identifier = when (codeReceivedViaType) {
                        CodeReceivedViaType.SMS -> "$identifierPrefix$phoneNumberOrEmail"
                        CodeReceivedViaType.EMAIL -> phoneNumberOrEmail
                    },
                    code = code,
                    locale = language ?: Language.ENGLISH
                )
                validatePasswordCode(
                    request,
                    validateCodeByPasswordIdentifierUseCase,
                    onSuccessAction = {
                        _baseCmd.value = BaseCommand.PerformNavAction(
                            ConfirmOneTimePasswordFragmentDirections
                                .actionConfirmOneTimePasswordFragmentToResetPasswordFragment(
                                    it.token
                                )
                        )
                    }
                )
            }
        }
    }

    private fun onContinueFromEditProfile() {
        performApiCall {
            verificationCode.value?.let { code ->
                val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                    Constants.PHONE_PREFIX_RO
                else
                    Constants.PHONE_PREFIX_US
                val request = UpdateIdentifiersCodeValidationRequest(
                    channel = when (codeReceivedViaType) {
                        CodeReceivedViaType.SMS -> IdentifierChannel.SMS
                        CodeReceivedViaType.EMAIL -> IdentifierChannel.EMAIL
                    },
                    identifier = when (codeReceivedViaType) {
                        CodeReceivedViaType.SMS -> "$identifierPrefix$phoneNumberOrEmail"
                        CodeReceivedViaType.EMAIL -> phoneNumberOrEmail
                    },
                    code = code
                )
                validateChangeCode(
                    request,
                    validateCodeByUpdateIdentifiersUseCase,
                    onSuccessAction = { user ->
                        userProfile = user
                        _baseCmd.value = BaseCommand.GoBackTo(R.id.editProfileFragment)
                    }
                )
            }
        }
    }

    private fun resendConfirmationCodeAndStartTimer() {
        clearInvalidCodeError()
        clearVerificationCode()
        initResendCode()
        performApiCall {
            val channel = when (codeReceivedViaType) {
                CodeReceivedViaType.SMS -> IdentifierChannel.SMS
                CodeReceivedViaType.EMAIL -> IdentifierChannel.EMAIL
            }
            val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                Constants.PHONE_PREFIX_RO
            else
                Constants.PHONE_PREFIX_US
            val request = IdentifiersRequest(
                channel = channel,
                identifier = when (codeReceivedViaType) {
                    CodeReceivedViaType.SMS -> "$identifierPrefix$phoneNumberOrEmail"
                    CodeReceivedViaType.EMAIL -> phoneNumberOrEmail
                },
                locale = language ?: Language.ENGLISH
            )
            when (fromEditProfile) {
                true -> resendConfirmationFromEditProfile(request)
                false -> resendConfirmationFromPasswordReset(request)
            }
        }
    }

    private suspend fun resendConfirmationFromEditProfile(request: IdentifiersRequest) {
        val result = submitChangeIdentifiersUseCase(request)
        result.onError {
            showApiError(it)
        }
    }

    private suspend fun resendConfirmationFromPasswordReset(request: IdentifiersRequest) {
        val result = submitPasswordIdentifiersUseCase(request)
        result.onError {
            showApiError(it)
        }
    }

    private fun showApiError(it: ApiError) {
        val error =
            it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
        if (it.errorCode == Constants.ERROR_CODE_422)
            _invalidCodeErrorVisible.value = error
        else
            _baseCmd.value = BaseCommand.ShowToast(error)
    }

    fun onResendConfirmationCodeClicked() {
        resendConfirmationCodeAndStartTimer()
    }
}
