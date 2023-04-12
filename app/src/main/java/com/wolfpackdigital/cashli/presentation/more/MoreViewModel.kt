package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class MoreViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    sealed class Command
}
