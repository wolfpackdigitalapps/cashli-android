package com.wolfpackdigital.cashli.shared.base

import android.app.AlertDialog
import android.app.Application
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.minusAssign
import com.wolfpackdigital.cashli.shared.utils.extensions.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
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
        if (showLoading) {
            _apiCallsCount += 1
            viewModelScope.launch {
                block()
                _apiCallsCount -= 1
            }
        } else viewModelScope.launch { block() }
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
    class ShowToastById(val stringId: Int) : BaseCommand()
    class ShowToast(val message: String) : BaseCommand()
    class ShowSnackbarById(val stringId: Int) : BaseCommand()
    class ShowSnackbar(val message: String) : BaseCommand()
    data class ShowPopupById(
        var titleId: Int,
        var contentId: Int? = null,
        var imageId: Int,
        var content: String? = null,
        var timerCount: Long? = null,
        var buttonPrimaryId: Int? = null,
        var buttonSecondaryId: Int? = null,
        var buttonPrimaryClick: (AlertDialog) -> Unit = {},
        var buttonSecondaryClick: (AlertDialog) -> Unit = {},
        var buttonCloseClick: ((AlertDialog) -> Unit)? = null,
        var contentFormatArgs: Array<Any>? = null
    ) : BaseCommand()

    class PerformNavAction(
        val direction: NavDirections,
        @IdRes val popUpTo: Int? = null,
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
    object ForceCloseKeyboard : BaseCommand()
}
