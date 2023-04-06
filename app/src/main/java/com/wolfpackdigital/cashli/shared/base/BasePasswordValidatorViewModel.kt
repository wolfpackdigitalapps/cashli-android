package com.wolfpackdigital.cashli.shared.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.hasPasswordPattern
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet

abstract class BasePasswordValidatorViewModel : BaseViewModel() {

    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()
    val isPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible = MutableLiveData(false)

    @Suppress("PropertyName", "VariableNaming")
    protected val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?> = _passwordError

    fun onPasswordVisibilityChange() {
        isPasswordVisible.value = isPasswordVisible.value?.not() ?: false
    }

    fun onConfirmPasswordVisibilityChange() {
        isConfirmPasswordVisible.value = isConfirmPasswordVisible.value?.not() ?: false
    }

    protected open fun validatePasswords(onValidInput: suspend () -> Unit) {
        safeLet(password.value, confirmPassword.value) { password, confirmPassword ->
            var error = false
            _passwordError.value = when {
                password != confirmPassword -> {
                    error = true
                    R.string.password_not_match
                }
                password.length < Constants.PASSWORD_MIN_LENGTH -> {
                    error = true
                    R.string.password_length
                }
                !password.hasPasswordPattern() -> {
                    error = true
                    R.string.password_format
                }
                else -> null
            }
            if (!error)
                performApiCall { onValidInput() }
        }
    }
}
