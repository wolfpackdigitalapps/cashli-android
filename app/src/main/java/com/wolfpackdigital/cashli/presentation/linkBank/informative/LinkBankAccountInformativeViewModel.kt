package com.wolfpackdigital.cashli.presentation.linkBank.informative

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class LinkBankAccountInformativeViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(onBack = ::back)
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    fun linkMyBankAccount() {
        // TODO
    }

    sealed class Command
}
