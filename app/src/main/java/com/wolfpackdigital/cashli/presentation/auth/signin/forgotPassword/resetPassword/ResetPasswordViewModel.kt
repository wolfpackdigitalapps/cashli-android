package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BasePasswordValidatorViewModel
import com.wolfpackdigital.cashli.shared.base.BaseViewModel


class ResetPasswordViewModel : BasePasswordValidatorViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.reset_password,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    fun onConfirmClicked() {

    }
}