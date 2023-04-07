package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseValidateCodeViewModel

class ConfirmOneTimePasswordViewModel : BaseValidateCodeViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.confirm_one_time_password,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    fun onContinueClicked() {}
}