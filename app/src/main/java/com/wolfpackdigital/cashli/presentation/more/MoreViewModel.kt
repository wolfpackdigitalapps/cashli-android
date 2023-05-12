package com.wolfpackdigital.cashli.presentation.more

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.usecases.LogOutUserUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.enums.MenuItem
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants.SIGN_IN_SCREEN_DL
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class MoreViewModel(
    private val logOutUserUseCase: LogOutUserUseCase
) : BaseViewModel(), PersistenceService {

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
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleId = R.string.log_out,
                        imageId = R.drawable.ic_log_out,
                        contentIdOrString = R.string.log_out_message,
                        buttonPrimaryId = R.string.yes_log_out,
                        isCloseVisible = false,
                        buttonSecondaryId = R.string.cancel,
                        buttonPrimaryClick = { handleLogOut() },
                    )
                )
            }
        }
    }

    fun goToEditProfileScreen() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            MoreFragmentDirections.actionMoreFragmentToEditProfileFragment()
        )
    }

    private fun handleLogOut() {
        performApiCall {
            val result = logOutUserUseCase(Unit)
            result.onSuccess {
                clearUserData()
                _baseCmd.value = BaseCommand.PerformNavDeepLink(
                    deepLink = SIGN_IN_SCREEN_DL,
                    popUpTo = R.id.navigation
                )
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    sealed class Command
}
