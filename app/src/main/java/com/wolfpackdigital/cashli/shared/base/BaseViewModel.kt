package com.wolfpackdigital.cashli.shared.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.minusAssign
import com.wolfpackdigital.cashli.shared.utils.extensions.plusAssign
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket

abstract class BaseViewModel : ViewModel() {
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

    @Suppress("MagicNumber")
    protected fun checkInternetAvailable(callback: (isInternet: Boolean) -> Unit) {
        performApiCall {
            try {
                val timeoutMs = 1500
                val socket = Socket()
                val socketAddress = InetSocketAddress("8.8.8.8", 53)

                withContext(Dispatchers.IO) {
                    socket.connect(socketAddress, timeoutMs)
                    socket.close()
                }

                callback(true)
            } catch (e: IOException) {
                callback(false)
            }
        }
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
    class PerformNavAction(val direction: NavDirections) : BaseCommand()
    object GoBack : BaseCommand()
}
