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

    private val _notificationsEnabled =
        MutableLiveData(
            userProfile?.userSettings?.find { userSetting ->
                userSetting.key == UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED
            }?.value.toBoolean()
        )
    val notificationsEnabled: LiveData<Boolean> = _notificationsEnabled

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
            val lowBalanceSetting = UserSetting(
                key = UserSettingsKeys.LOW_BALANCE_THRESHOLD,
                value = newAmount
            )
            handleUserSettingChanged(
                lowBalanceSetting,
                onSuccess = { newUserSettings ->
                    _amount.value = newUserSettings.value
                }
            )
        }
    }

    private fun handleUserSettingChanged(
        userSetting: UserSetting,
        onSuccess: (UserSetting) -> Unit = {},
        onError: () -> Unit = {}
    ) {
        performApiCall {
            val result = updateUserSettingUseCase(userSetting)
            result.onSuccess { newUserSettings ->
                userProfile =
                    userProfile?.copy(
                        userSettings = userProfile?.userSettings?.map { oldUserSettings ->
                            if (newUserSettings.key == oldUserSettings.key) newUserSettings
                            else oldUserSettings
                        } ?: listOf()
                    )
                onSuccess.invoke(userSetting)
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
                onError.invoke()
            }
            if (userSetting.key == UserSettingsKeys.LOW_BALANCE_THRESHOLD)
                _cmd.value = Command.TransitionToStart
        }
    }

    fun onTogglePushNotifications() {
        val userNotificationsSetting = userProfile?.userSettings?.find { userSetting ->
            userSetting.key == UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED
        }?.value.toBoolean()
        if (userNotificationsSetting)
            toggleNotificationSetting(isEnabled = false)
        else
            _cmd.value = Command.CheckPushNotificationPermissions
    }

    fun enableNotifications() {
        toggleNotificationSetting(isEnabled = true)
    }

    private fun toggleNotificationSetting(isEnabled: Boolean) {
        val userNotificationsSetting = UserSetting(
            key = UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED,
            value = isEnabled.toString()
        )
        handleUserSettingChanged(
            userNotificationsSetting,
            onSuccess = { newUserSettings ->
                _notificationsEnabled.value = newUserSettings.value.toBoolean()
            }
        )
    }

    sealed class Command {
        object TransitionToEnd : Command()
        object TransitionToStart : Command()
        object CheckPushNotificationPermissions : Command()
    }
}
