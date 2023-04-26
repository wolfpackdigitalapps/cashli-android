package com.wolfpackdigital.cashli.shared.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.domain.entities.response.PasswordIdentifierToken
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByIdentifierUseCase
import com.wolfpackdigital.cashli.domain.usecases.ValidateCodeByPasswordIdentifierUseCase
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_422
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_429
import com.wolfpackdigital.cashli.shared.utils.extensions.initTimer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

private const val COUNT_DOWN_TIME = 60
private const val VERIFICATION_CODE_VALID_LENGTH = 4

abstract class BaseValidateCodeViewModel : BaseViewModel() {

    val verificationCode = MutableLiveData<String?>()

    private val _timerResendCounter = MutableLiveData<Int?>()
    val timerResendCounter: LiveData<Int?> = _timerResendCounter

    private val _timerResendEnabled = MutableLiveData(false)
    val timerResendEnabled: LiveData<Boolean> = _timerResendEnabled

    @Suppress("PropertyName", "VariableNaming")
    protected val _invalidCodeErrorVisible = MutableLiveData<Any?>()
    val invalidCodeErrorVisible: LiveData<Any?> = _invalidCodeErrorVisible

    @Suppress("PropertyName", "VariableNaming")
    protected val _buttonsEnabled = MutableLiveData(true)
    val buttonsEnabled: LiveData<Boolean> = _buttonsEnabled

    val codeValidateButtonEnabled =
        combine(verificationCode.asFlow(), invalidCodeErrorVisible.asFlow()) { code, error ->
            code?.length == VERIFICATION_CODE_VALID_LENGTH && error == null
        }.asLiveData()
    private var job: Job? = null

    protected fun initResendCode() {
        job = viewModelScope.launch {
            initTimer(COUNT_DOWN_TIME).onCompletion {
                updateCounterVariables()
                cancelStepSwipeJob()
            }.collect {
                updateCounterVariables(resendEnable = false, counterValue = it)
            }
        }
    }

    private fun updateCounterVariables(resendEnable: Boolean = true, counterValue: Int? = null) {
        _timerResendEnabled.value = resendEnable
        _timerResendCounter.value = counterValue
    }

    private fun cancelStepSwipeJob() {
        job?.cancel()
        job = null
    }

    fun clearInvalidCodeError() {
        _invalidCodeErrorVisible.value = null
    }

    fun clearVerificationCode() {
        verificationCode.value = null
    }

    open fun resendConfirmationCodeAndStartTimer(
        identifier: String?,
        codeReceivedViaType: CodeReceivedViaType,
        submitRegistrationIdentifiersUseCase: SubmitRegistrationIdentifiersUseCase
    ) {
        clearInvalidCodeError()
        clearVerificationCode()
        initResendCode()
        performApiCall {
            identifier?.let { identifier ->
                val channel = when (codeReceivedViaType) {
                    CodeReceivedViaType.SMS -> IdentifierChannel.SMS
                    CodeReceivedViaType.EMAIL -> IdentifierChannel.EMAIL
                }
                val request = IdentifiersRequest(
                    channel = channel,
                    identifier = identifier
                )
                val result = submitRegistrationIdentifiersUseCase(request)
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    if (it.errorCode == ERROR_CODE_422)
                        _invalidCodeErrorVisible.value = error
                    else
                        _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    open fun validateCode(
        request: IdentifiersCodeValidationRequest,
        validateCodeByIdentifierUseCase: ValidateCodeByIdentifierUseCase,
        onSuccessAction: (IdentifierToken) -> Unit = {},
        onErrorAction: (ApiError) -> Unit = { onValidateCodeError(it) }
    ) {
        performApiCall {
            val result = validateCodeByIdentifierUseCase(request)
            result.onSuccess {
                onSuccessAction(it)
            }
            result.onError {
                onErrorAction(it)
            }
        }
    }

    open fun validatePasswordCode(
        request: IdentifiersCodeValidationRequest,
        validateCodeByPasswordIdentifierUseCase: ValidateCodeByPasswordIdentifierUseCase,
        onSuccessAction: (PasswordIdentifierToken) -> Unit = {},
        onErrorAction: (ApiError) -> Unit = { onValidateCodeError(it) }
    ) {
        performApiCall {
            val result = validateCodeByPasswordIdentifierUseCase(request)
            result.onSuccess {
                onSuccessAction(it)
            }
            result.onError {
                onErrorAction(it)
            }
        }
    }

    private fun onValidateCodeError(it: ApiError) {
        val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
        when (it.errorCode) {
            ERROR_CODE_422 ->
                _invalidCodeErrorVisible.value = error
            ERROR_CODE_429 -> {
                _buttonsEnabled.value = false
                updateCounterVariables()
                cancelStepSwipeJob()
                _invalidCodeErrorVisible.value = error
            }
            else ->
                _baseCmd.value = BaseCommand.ShowToast(error)
        }
    }
}
