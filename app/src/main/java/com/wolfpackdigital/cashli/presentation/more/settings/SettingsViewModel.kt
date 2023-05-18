package com.wolfpackdigital.cashli.presentation.more.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.domain.entities.enums.UserSettingsKeys
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserSettingUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class SettingsViewModel(
    private val updateUserSettingUseCase: UpdateUserSettingUseCase
) : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.settings,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private var isAmountEditable = false

    val newAmount = MutableLiveData(
        userProfile?.userSettings?.find { userSetting ->
            userSetting.key == UserSettingsKeys.LOW_BALANCE_THRESHOLD
        }?.value ?: EMPTY_STRING
    )

    private val _amount = MutableLiveData(newAmount.value ?: EMPTY_STRING)
    val amount: LiveData<String> = _amount

    fun toggleChangeAmount() {
        newAmount.value?.let { newAmount ->
            if (isAmountEditable) {
                closeKeyboardAndClearFocus()
                handleNewAmount(newAmount)
            } else
                _cmd.value = Command.TransitionToEnd
            isAmountEditable = isAmountEditable.not()
        }
    }

    private fun handleNewAmount(newAmount: String) {
        if (newAmount.isNotBlank()) {
            performApiCall {
                val result = updateUserSettingUseCase(
                    UserSetting(
                        key = UserSettingsKeys.LOW_BALANCE_THRESHOLD,
                        value = newAmount
                    )
                )
                result.onSuccess { newUserSettings ->
                    userProfile =
                        userProfile?.copy(
                            userSettings = userProfile?.userSettings?.map { oldUserSettings ->
                                if (newUserSettings.key == oldUserSettings.key) newUserSettings
                                else oldUserSettings
                            } ?: listOf()
                        )
                    _amount.value = newUserSettings.value
                }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
                _cmd.value = Command.TransitionToStart
            }
        }
    }

    sealed class Command {
        object TransitionToEnd : Command()
        object TransitionToStart : Command()
    }
}
