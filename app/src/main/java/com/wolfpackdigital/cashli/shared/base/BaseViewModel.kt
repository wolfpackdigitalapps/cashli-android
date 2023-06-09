package com.wolfpackdigital.cashli.shared.base

import android.app.Application
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.shared.exceptions.RefreshTokenExpiredException
import com.wolfpackdigital.cashli.shared.utils.Constants.SIGN_IN_SCREEN_DL
import com.wolfpackdigital.cashli.shared.utils.Constants.SUPPORT_PHONE_NUMBER
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.minusAssign
import com.wolfpackdigital.cashli.shared.utils.extensions.plusAssign
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel(), PersistenceService {
    @Suppress("PropertyName", "VariableNaming")
    protected val _apiCallsCount = MutableLiveData<Int>()
    val apiCallsCount: LiveData<Int>
        get() = _apiCallsCount

    @Suppress("PropertyName", "VariableNaming")
    protected val _baseCmd = LiveEvent<BaseCommand>()
    val baseCmd: LiveData<BaseCommand>
        get() = _baseCmd

    protected fun performApiCall(
        showLoading: Boolean = true,
        block: suspend CoroutineScope.() -> Unit
    ) {
        viewModelScope.launch {
            try {
                if (showLoading) _apiCallsCount += 1
                block()
            } catch (ex: RefreshTokenExpiredException) {
                displaySessionExpiredMessage()
                clearDataAndRedirectToLogin()
            } finally {
                if (showLoading) _apiCallsCount -= 1
            }
        }
    }

    private fun displaySessionExpiredMessage() {
        _baseCmd.value = BaseCommand.ShowToast(R.string.generic_session_expired)
    }

    protected fun clearDataAndRedirectToLogin() {
        clearUserData()
        _baseCmd.value = BaseCommand.PerformNavDeepLink(
            deepLink = SIGN_IN_SCREEN_DL,
            popUpTo = R.id.navigation,
            inclusive = true
        )
    }

    open fun back() {
        _baseCmd.value = BaseCommand.GoBack
    }

    open fun closeKeyboardAndClearFocus() {
        _baseCmd.value = BaseCommand.ForceCloseKeyboard
    }
}

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    @Suppress("PropertyName", "VariableNaming")
    protected val _apiCallsCount = MutableLiveData<Int>()
    val apiCallsCount: LiveData<Int>
        get() = _apiCallsCount

    @Suppress("PropertyName", "VariableNaming")
    protected val _baseCmd = LiveEvent<BaseCommand>()
    val baseCmd: LiveData<BaseCommand>
        get() = _baseCmd

    protected fun performApiCall(block: suspend CoroutineScope.() -> Unit) {
        _apiCallsCount += 1
        viewModelScope.launch {
            block()
            _apiCallsCount -= 1
        }
    }
}

sealed class BaseCommand {
    class OpenUrl(@StringRes val urlResource: Int) : BaseCommand()
    class ShowToast(val message: Any) : BaseCommand()
    class ShowSnackbar(val message: Any) : BaseCommand()
    data class ShowPopupById(val popupConfig: PopupConfig) : BaseCommand()

    class PerformNavDeepLink(
        val deepLink: String,
        @IdRes val popUpTo: Int? = null,
        val popUpToRoot: Boolean = false,
        val inclusive: Boolean = false
    ) : BaseCommand()

    class PerformNavAction(
        val direction: NavDirections,
        @IdRes val popUpTo: Int? = null,
        val popUpToRoot: Boolean = false,
        val inclusive: Boolean = false
    ) : BaseCommand()

    data class PerformNavById(
        val currentDestinationId: Int,
        val destinationId: Int,
        val bundle: Bundle = bundleOf(),
        val options: NavOptions? = null,
        val extras: Navigator.Extras? = null
    ) : BaseCommand()

    object GoBack : BaseCommand()

    class GoBackTo(
        @IdRes val destinationId: Int,
        val inclusive: Boolean = false
    ) : BaseCommand()

    data class OpenSMSApp(
        val phoneNumber: String = SUPPORT_PHONE_NUMBER
    ) : BaseCommand()

    data class OpenPhoneApp(
        val phoneNumber: String = SUPPORT_PHONE_NUMBER
    ) : BaseCommand()

    object ForceCloseKeyboard : BaseCommand()
}
