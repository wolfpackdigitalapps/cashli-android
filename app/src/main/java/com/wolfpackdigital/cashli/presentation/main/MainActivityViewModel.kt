package com.wolfpackdigital.cashli.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.enums.UserSettingsKeys
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.notifications.NotificationModel
import com.wolfpackdigital.cashli.shared.notifications.PushNotificationAction
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_SCREEN_DELAY = 2000L

class MainActivityViewModel : BaseViewModel() {
    private val _keepShowingSplash = MutableLiveData<Boolean>()
    val keepShowingSplash: LiveData<Boolean> = _keepShowingSplash

    private val _isUserLogged = MutableLiveData(false)
    val isUserLogged: LiveData<Boolean> = _isUserLogged

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _isBottomBarVisible = MutableLiveData(true)
    val isBottomBarVisible: LiveData<Boolean> = _isBottomBarVisible

    private var currentDestinationId: Int? = null

    val destinationChangeListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            currentDestinationId = destination.id
            val showBottomNav = when (destination.id) {
                R.id.homeFragment,
                R.id.accountFragment,
                R.id.moreFragment,
                R.id.editProfileFragment,
                R.id.changePhoneOrEmailFragment,
                R.id.deleteAccountDialog,
                R.id.settingsFragment,
                R.id.helpFragment -> true

                else -> false
            }
            setBottomBarVisibility(showBottomNav)
        }

    init {
        viewModelScope.launch {
            userProfile?.let {
                _isUserLogged.value = true
            }
            delay(SPLASH_SCREEN_DELAY)
            _keepShowingSplash.value = false
        }
    }

    private fun setBottomBarVisibility(visible: Boolean) {
        _isBottomBarVisible.value = visible
    }

    fun handlePushNotificationType(notificationModel: NotificationModel?) {
        when {
            currentDestinationId == R.id.linkBankAccountInformativeFragment -> return
            currentDestinationId == R.id.accountFragment &&
                userProfile?.eligibilityStatus == EligibilityStatus.ELIGIBILITY_CHECK_PENDING &&
                userProfile?.bankAccount == null &&
                notificationModel?.type == PushNotificationAction.USER_ELIGIBLE -> return
        }

        val popupConfig = when (notificationModel?.type) {
            PushNotificationAction.LOW_BALANCE -> {
                PopupConfig(
                    titleIdOrString = notificationModel.title ?: R.string.low_balance,
                    imageId = R.drawable.ic_warning,
                    contentIdOrString = notificationModel.message ?: R.string.bank_balance_below,
                    contentFormatArgs = handleLocalUserSettingBalance()
                )
            }

            PushNotificationAction.USER_ELIGIBLE -> {
                PopupConfig(
                    titleIdOrString = notificationModel.title ?: R.string.congrats,
                    imageId = R.drawable.ic_congrats,
                    contentIdOrString = notificationModel.message ?: R.string.you_are_eligible_now,
                    buttonCloseClick = {
                        _cmd.value = Command.RefreshUserProfileData
                    }
                )
            }

            PushNotificationAction.LINK_ACCOUNT_CONNECTION_LOST -> {
                PopupConfig(
                    titleIdOrString = notificationModel.title ?: R.string.link_account,
                    imageId = R.drawable.ic_warning,
                    contentIdOrString = notificationModel.message
                        ?: R.string.bank_account_connection_lost,
                    buttonCloseClick = {
                        _cmd.value = Command.RefreshUserProfileData
                    }
                )
            }

            else -> null
        }
        popupConfig?.let { _baseCmd.value = BaseCommand.ShowPopupById(it) }
    }

    private fun handleLocalUserSettingBalance(): Array<Any>? {
        val balanceBelow = userProfile?.userSettings?.find { userSetting ->
            userSetting.key == UserSettingsKeys.LOW_BALANCE_THRESHOLD
        }?.value
        return balanceBelow?.let { arrayOf(it) }
    }

    sealed class Command {
        object RefreshUserProfileData : Command()
    }
}
