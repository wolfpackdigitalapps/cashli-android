package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.ResetPasswordRequest
import com.wolfpackdigital.cashli.domain.usecases.ResetPasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChoosePasswordFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import kotlinx.coroutines.flow.combine

class ResetPasswordViewModel(
    private val resetToken: String,
    private val choosePasswordFormUseCase: ValidateChoosePasswordFormUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.reset_password,
            isBackVisible = true,
            onBack = ::backToRequestCode
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?> = _passwordError

    private val _isPasswordVisible = MutableLiveData(false)
    val isPasswordVisible: LiveData<Boolean> = _isPasswordVisible

    private val _isConfirmPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible: LiveData<Boolean> = _isConfirmPasswordVisible

    val isConfirmEnabled =
        combine(password.asFlow(), confirmPassword.asFlow()) { password, confirmPassword ->
            !password.isNullOrBlank() && !confirmPassword.isNullOrBlank()
        }.asLiveData()

    fun onPasswordVisibilityChanged() {
        _isPasswordVisible.value = _isPasswordVisible.value?.not()
    }

    fun onConfirmPasswordVisibilityChanged() {
        _isConfirmPasswordVisible.value = _isConfirmPasswordVisible.value?.not()
    }

    private fun validatePasswords(onValidInput: suspend (String, String) -> Unit) {
        safeLet(password.value, confirmPassword.value) { password, confirmPassword ->
            val validatePasswordsResult = choosePasswordFormUseCase(
                password = password,
                confirmPassword = confirmPassword
            )
            if (!validatePasswordsResult.successful) {
                _passwordError.value = validatePasswordsResult.errorMessageId
            } else {
                performApiCall {
                    onValidInput(password, confirmPassword)
                }
            }
        }
    }

    fun onConfirmClicked() {
        validatePasswords { password, confirmPassword ->
            val request = ResetPasswordRequest(
                token = resetToken,
                password = password,
                confirmPassword = confirmPassword
            )
            val result = resetPasswordUseCase(request)
            result.onSuccess {
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleId = R.string.bravo_text,
                        imageId = R.drawable.ic_guard_check,
                        contentIdOrString = R.string.password_reset_success,
                        timerCount = Constants.COUNT_DOWN_TIME_6_SEC,
                        buttonCloseClick = {
                            navigateToSignIn()
                        }
                    )
                )
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun navigateToSignIn() {
        _baseCmd.value = BaseCommand.GoBackTo(R.id.signInFragment)
    }

    fun clearPasswordError() {
        _passwordError.value = null
    }

    fun backToRequestCode() {
        _baseCmd.value = BaseCommand.GoBackTo(R.id.requestCodeFragment)
    }
}
