package com.wolfpackdigital.cashli.presentation.help

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

private const val SUPPORT_EMAIL = "support@cashli.io"

class HelpViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    fun openSmsApp() {
        _baseCmd.value = BaseCommand.OpenSMSApp()
    }

    fun openEmailApp() {
        _cmd.value = Command.OpenEmailApp(SUPPORT_EMAIL)
    }

    sealed class Command {
        data class OpenEmailApp(val email: String): Command()
    }

}