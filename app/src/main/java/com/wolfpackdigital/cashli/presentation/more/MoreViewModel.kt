package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.enums.MenuItem
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class MoreViewModel : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    val firstName = userProfile?.firstName
    val lastName = userProfile?.lastName

    // TODO replace content of filter when account status is known(pause or unpause)
    private val _menuItems =
        MutableLiveData(MenuItem.values().filter { it.title != R.string.unpause_close_account })
    val menuItems: LiveData<List<MenuItem>> = _menuItems

    fun handleOnMenuItemClicked(menuItem: MenuItem) {
        when (menuItem) {
            MenuItem.MEMBERSHIP_ADVANCE_HISTORY -> {
                // TODO new card
            }

            MenuItem.TERMS_AND_CONDITIONS -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.terms_and_conditions_url)
            }

            MenuItem.PRIVACY_POLICY -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.privacy_policy_url)
            }

            MenuItem.SETTINGS -> {
                // TODO new card
            }

            MenuItem.HELP -> {
                // TODO new card
            }

            MenuItem.REFER_FRIEND -> {
                // TODO new card
            }

            MenuItem.FAQ -> {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.faq_url)
            }

            MenuItem.PAUSE_CLOSE_ACCOUNT -> {
                // TODO new card
            }

            MenuItem.UNPAUSE_CLOSE_ACCOUNT -> {
                // TODO new card
            }

            MenuItem.LOGOUT -> {
                // TODO new card
            }
        }
    }

    sealed class Command
}
