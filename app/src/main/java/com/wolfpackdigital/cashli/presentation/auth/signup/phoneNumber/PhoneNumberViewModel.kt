package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class PhoneNumberViewModel : BaseViewModel() {

    private val _toolbar = MutableLiveData<Toolbar>()
    val toolbar: LiveData<Toolbar> = _toolbar

    init {
        _toolbar.value = Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            onBack = ::back
        )
    }
}