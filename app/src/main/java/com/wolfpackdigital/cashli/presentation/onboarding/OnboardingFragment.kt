package com.wolfpackdigital.cashli.presentation.onboarding

import android.annotation.SuppressLint
import androidx.viewpager2.widget.ViewPager2
import com.wolfpackdigital.cashli.OnboardingBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.OnboardingStep
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants.FIRST_INDEX
import com.wolfpackdigital.cashli.shared.utils.Constants.RESTART_ONBOARDING_STEPS
import com.wolfpackdigital.cashli.shared.utils.extensions.getBackStackData
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.showSMSApp
import org.koin.androidx.viewmodel.ext.android.viewModel

class OnboardingFragment :
    BaseFragment<OnboardingBinding, OnboardingViewModel>(R.layout.fr_onboarding) {

    override val viewModel by viewModel<OnboardingViewModel>()

    private val onPageChanged = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewModel.pageSelected(position)
        }
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) {
            val currentItem = binding?.viewPager?.currentItem ?: 0
            when (it) {
                OnboardingViewModel.Command.OnNext ->
                    binding?.viewPager?.currentItem = currentItem.inc()
                OnboardingViewModel.Command.GetSupport -> activity?.showSMSApp()
            }
        }
        viewModel.onboardingSteps.observe(viewLifecycleOwner) {
            setupViewPager(it)
        }
        navController?.getBackStackData<Boolean>(
            RESTART_ONBOARDING_STEPS,
            viewLifecycleOwner,
            removeValue = true
        ) {
            if (it == true) binding?.viewPager?.currentItem = FIRST_INDEX
        }
    }

    @SuppressLint("WrongConstant")
    private fun setupViewPager(steps: List<OnboardingStep>) {
        binding?.viewPager?.apply {
            adapter = OnboardingPagerAdapter(this@OnboardingFragment, steps)
            registerOnPageChangeCallback(onPageChanged)
            binding?.dotsIndicator?.attachTo(this)
        }
    }

    override fun onDestroyView() {
        binding?.viewPager?.unregisterOnPageChangeCallback(onPageChanged)
        super.onDestroyView()
    }
}
