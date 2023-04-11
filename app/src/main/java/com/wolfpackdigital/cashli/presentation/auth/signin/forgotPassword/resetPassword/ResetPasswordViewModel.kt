package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BasePasswordValidatorViewModel
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import kotlinx.coroutines.flow.combine


class ResetPasswordViewModel : BasePasswordValidatorViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.reset_password,
            isBackVisible = true,
            onBack = ::backToRequestCode
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val isConfirmEnabled =
        combine(password.asFlow(), confirmPassword.asFlow()) { password, confirmPassword ->
            !password.isNullOrBlank() && !confirmPassword.isNullOrBlank()
        }.asLiveData()

    fun onConfirmClicked() {

    }

    private fun backToRequestCode() {
        _baseCmd.value = BaseCommand.GoBackTo(R.id.requestCodeFragment)
    }
}