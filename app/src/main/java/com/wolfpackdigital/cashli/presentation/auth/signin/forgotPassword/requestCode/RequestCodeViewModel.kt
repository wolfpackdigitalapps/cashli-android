package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class RequestCodeViewModel : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.forgot_password,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    fun onContinueClicked() {

    }
}