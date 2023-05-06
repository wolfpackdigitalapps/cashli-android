package com.wolfpackdigital.cashli.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class HomeViewModel : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar
    val name = userProfile?.firstName

    fun goToLinkBankAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeFragmentDirections.actionGlobalLinkAccountGraph()
        )
    }

    fun goToClaimCash() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeFragmentDirections.actionHomeFragmentToClaimCashFragment()
        )
    }

    companion object {
        const val SUM_150 = "150"
    }

    sealed class Command
}
