package com.wolfpackdigital.cashli.presentation.claimCash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class ClaimCashViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    val amountPerc = MutableLiveData(0)

    private val _amount = MutableLiveData(amountPerc.value?.toFloat() ?: 0f)
    val amount: LiveData<Float> = _amount

    fun saveAmount() {
        amountPerc.value?.let {
            _amount.value = it.toFloat()
            _cmd.value = Command.TransitionToStart
        }
    }

    sealed class Command {
        object TransitionToStart : Command()
    }

}