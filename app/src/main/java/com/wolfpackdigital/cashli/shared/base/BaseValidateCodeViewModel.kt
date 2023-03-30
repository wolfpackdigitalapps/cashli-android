package com.wolfpackdigital.cashli.shared.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
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
    protected val _invalidCodeErrorVisible = MutableLiveData<Int?>()
    val invalidCodeErrorVisible: LiveData<Int?> = _invalidCodeErrorVisible

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
}
