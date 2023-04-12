package com.wolfpackdigital.cashli.presentation.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.domain.usecases.GetOnboardingStepsUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.successOr
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.initTimer
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

private const val ONBOARDING_FIRST_STEP_INDEX = 0
private const val ONBOARDING_STEP_SWIPE_DELAY = 4

class OnboardingViewModel(
    private val getOnboardingStepsUseCase: GetOnboardingStepsUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _onboardingSteps = MutableLiveData<List<OnboardingStep>>()
    val onboardingSteps: LiveData<List<OnboardingStep>> = _onboardingSteps

    private val _currentStep = MutableLiveData(ONBOARDING_FIRST_STEP_INDEX)
    private var totalSteps: Int = ONBOARDING_FIRST_STEP_INDEX
    private var job: Job? = null

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    init {
        performApiCall(showLoading = false) {
            _onboardingSteps.value =
                getOnboardingStepsUseCase.executeNow(Unit).successOr(null).also {
                    totalSteps = it?.size ?: ONBOARDING_FIRST_STEP_INDEX
                }
        }
    }

    private fun toggleStepSwipeJob() {
        viewModelScope.launch {
            job?.let {
                cancelStepSwipeJob()
                checkIfStepSwipeJobNeedToToggle()
            } ?: run { initStepSwipeJob() }
        }
    }

    private fun initStepSwipeJob() {
        job = initTimer(ONBOARDING_STEP_SWIPE_DELAY).onCompletion {
            if (it == null) _cmd.value = Command.OnNext
            cancelStepSwipeJob()
        }.launchIn(viewModelScope)
    }

    private fun cancelStepSwipeJob() {
        job?.cancel()
        job = null
    }

    fun pageSelected(page: Int) {
        _currentStep.value = page
        checkIfStepSwipeJobNeedToToggle()
    }

    private fun checkIfStepSwipeJobNeedToToggle() {
        _currentStep.value?.let { currentStep ->
            if (currentStep < totalSteps - 1) toggleStepSwipeJob()
        }
    }

    fun signIn() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            OnboardingFragmentDirections.actionOnboardingFragmentToSignInFragment()
        )
    }

    fun createAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            OnboardingFragmentDirections.actionOnboardingFragmentToInformativeFragment()
        )
    }

    fun getSupport() {
        _cmd.value = Command.GetSupport
    }

    sealed class Command {
        object OnNext : Command()
        object GetSupport : Command()
    }
}
