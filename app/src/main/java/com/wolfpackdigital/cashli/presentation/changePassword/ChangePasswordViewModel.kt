package com.wolfpackdigital.cashli.presentation.changePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.ChangePasswordRequest
import com.wolfpackdigital.cashli.domain.usecases.ChangePasswordUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChangePasswordPasswordFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_401
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import kotlinx.coroutines.flow.combine

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val validatePasswordUseCase: ValidateChangePasswordPasswordFormUseCase
) : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.change_password_toolbar_title,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val currentPassword = MutableLiveData<String>()
    val newPassword = MutableLiveData<String>()
    val confirmNewPassword = MutableLiveData<String>()

    private val _currentPasswordVisible = MutableLiveData(false)
    val currentPasswordVisible: LiveData<Boolean> = _currentPasswordVisible

    private val _newPasswordVisible = MutableLiveData(false)
    val newPasswordVisible: LiveData<Boolean> = _newPasswordVisible

    private val _confirmNewPasswordVisible = MutableLiveData(false)
    val confirmNewPasswordVisible: LiveData<Boolean> = _confirmNewPasswordVisible

    private val _currentPasswordError = MutableLiveData<Any?>(null)
    val currentPasswordError: MutableLiveData<Any?> = _currentPasswordError

    private val _newPasswordError = MutableLiveData<Any?>(null)
    val newPasswordError: LiveData<Any?> = _newPasswordError

    private val _confirmNewPasswordError = MutableLiveData<Any?>(null)
    val confirmNewPasswordError: LiveData<Any?> = _confirmNewPasswordError

    val isConfirmEnabled = combine(
        currentPassword.asFlow(),
        newPassword.asFlow(),
        confirmNewPassword.asFlow()
    ) { password, confirmPassword, confirmNewPassword ->
        !password.isNullOrBlank() && !confirmPassword.isNullOrBlank() && !confirmNewPassword.isNullOrBlank()
    }.asLiveData()

    fun toggleCurrentPasswordVisible() {
        _currentPasswordVisible.value = _currentPasswordVisible.value?.not()
    }

    fun toggleNewPasswordVisible() {
        _newPasswordVisible.value = _newPasswordVisible.value?.not()
    }

    fun toggleConfirmNewPassword() {
        _confirmNewPasswordVisible.value = _confirmNewPasswordVisible.value?.not()
    }

    private fun validatePasswords(onValidInput: suspend (String, String, String) -> Unit) {
        safeLet(
            currentPassword.value,
            newPassword.value,
            confirmNewPassword.value,
        ) { currentPassword, newPassword, confirmNewPassword ->
            val validatePasswordsResult = validatePasswordUseCase(
                currentPassword = currentPassword,
                newPassword = newPassword,
                confirmNewPassword = confirmNewPassword
            )
            if (!validatePasswordsResult.successful) {
                when (val error = validatePasswordsResult.errorMessageId) {
                    R.string.password_not_match -> _confirmNewPasswordError.value = error
                    else -> _newPasswordError.value = error
                }
            } else {
                performApiCall {
                    onValidInput(currentPassword, newPassword, confirmNewPassword)
                }
            }
        }
    }

    fun onContinueClicked() {
        validatePasswords { currentPassword, newPassword, confirmNewPassword ->
            val request = ChangePasswordRequest(
                password = currentPassword,
                newPassword = newPassword,
                newPasswordConfirmation = confirmNewPassword
            )
            val result = changePasswordUseCase(request)
            result.onSuccess {
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleIdOrString = R.string.bravo_text,
                        imageId = R.drawable.ic_guard_check,
                        contentIdOrString = R.string.change_password_passwords_changed,
                        timerCount = Constants.COUNT_DOWN_TIME_6_SEC,
                        buttonCloseClick = {
                            back()
                        }
                    )
                )
            }
            result.onError {
                when (it.errorCode) {
                    ERROR_CODE_401 -> _currentPasswordError.value = it.errors?.firstOrNull()
                    else -> _baseCmd.value = BaseCommand.ShowToast(
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    )
                }
            }
        }
    }

    fun clearCurrentPasswordError() {
        _currentPasswordError.value = null
    }

    fun clearNewPasswordError() {
        _newPasswordError.value = null
    }

    fun clearConfirmNewPasswordError() {
        _confirmNewPasswordError.value = null
    }
}
