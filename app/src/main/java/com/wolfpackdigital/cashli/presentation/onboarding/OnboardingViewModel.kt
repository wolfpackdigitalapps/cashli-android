package com.wolfpackdigital.cashli.presentation.onboarding

import androidx.lifecycle.LiveData
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class OnboardingViewModel : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd


    sealed class Command
}
