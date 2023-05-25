package com.wolfpackdigital.cashli.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_SCREEN_DELAY = 2000L

class MainActivityViewModel : ViewModel(), PersistenceService {
    private val _keepShowingSplash = MutableLiveData<Boolean>()
    val keepShowingSplash: LiveData<Boolean> = _keepShowingSplash

    private val _isUserLogged = MutableLiveData(false)
    val isUserLogged: LiveData<Boolean> = _isUserLogged

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _isBottomBarVisible = MutableLiveData(true)
    val isBottomBarVisible: LiveData<Boolean> = _isBottomBarVisible

    val destinationChangeListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            val showBottomNav = when (destination.id) {
                R.id.homeFragment,
                R.id.accountFragment,
                R.id.moreFragment,
                R.id.editProfileFragment,
                R.id.changePhoneOrEmailFragment,
                R.id.claimCashFragment,
                R.id.deleteAccountDialog,
                R.id.quizFragment,
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

    sealed class Command
}
