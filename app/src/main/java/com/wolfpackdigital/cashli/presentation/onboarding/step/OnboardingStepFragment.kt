package com.wolfpackdigital.cashli.presentation.onboarding.step

import androidx.core.os.bundleOf
import com.wolfpackdigital.cashli.OnboardingStepBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.extraNotNull
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

private const val ONBOARDING_STEP_MODEL = "onboarding_step_model"

class OnboardingStepFragment :
    BaseFragment<OnboardingStepBinding, OnboardingStepViewModel>(R.layout.fr_onboarding_step) {

    private val onboardingStep by extraNotNull<OnboardingStep>(ONBOARDING_STEP_MODEL)

    override val viewModel by viewModel<OnboardingStepViewModel> {
        parametersOf(onboardingStep)
    }

    override fun setupViews() {}

    companion object {
        fun newInstance(onboardingStep: OnboardingStep): OnboardingStepFragment {
            return OnboardingStepFragment().apply {
                arguments = bundleOf(ONBOARDING_STEP_MODEL to onboardingStep)
            }
        }
    }
}
