package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants

class PhoneNumberViewModel() : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = Constants.STEP_1,
            isStepCounterVisible = true,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

}