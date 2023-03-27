package com.wolfpackdigital.cashli.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.presentation.onboarding.step.OnboardingStepFragment

class OnboardingPagerAdapter(
    fragment: Fragment,
    private val onboardingSteps: List<OnboardingStep>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = onboardingSteps.size

    override fun createFragment(position: Int) =
        OnboardingStepFragment.newInstance(onboardingSteps[position])
}
