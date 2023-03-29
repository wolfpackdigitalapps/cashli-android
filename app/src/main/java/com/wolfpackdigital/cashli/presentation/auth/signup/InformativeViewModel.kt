package com.wolfpackdigital.cashli.presentation.auth.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel

class InformativeViewModel : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    fun signIn() {
        // TODO
    }

    fun signUp() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            InformativeFragmentDirections.actionInformativeFragmentToPhoneNumberFragment()
        )
    }
}
