package com.wolfpackdigital.cashli.presentation.home

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class HomeViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    fun goToLinkBankAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeFragmentDirections.actionGlobalLinkAccountGraph()
        )
    }

    sealed class Command
}
