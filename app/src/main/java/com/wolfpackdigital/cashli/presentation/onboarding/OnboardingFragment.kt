package com.wolfpackdigital.cashli.presentation.onboarding

import com.wolfpackdigital.cashli.OnboardingBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment : BaseFragment<OnboardingBinding, BaseViewModel>(R.layout.fr_onboarding) {

    override val viewModel by viewModel<OnboardingViewModel>()

    override fun setupViews() {
    }
}
