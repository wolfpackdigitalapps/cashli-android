package com.wolfpackdigital.cashli.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val SPLASH_SCREEN_DELAY = 2000L

class MainActivityViewModel : ViewModel() {
    private val _keepShowingSplash = MutableLiveData<Boolean>()
    val keepShowingSplash: LiveData<Boolean> = _keepShowingSplash

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DELAY)
            _keepShowingSplash.value = false
        }
    }
}
