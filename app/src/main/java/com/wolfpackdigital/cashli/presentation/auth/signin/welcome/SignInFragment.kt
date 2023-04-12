package com.wolfpackdigital.cashli.presentation.auth.signin.welcome

import androidx.activity.OnBackPressedCallback
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.SignInBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants.RESTART_ONBOARDING_STEPS
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.setAlphaAnimationVisibility
import com.wolfpackdigital.cashli.shared.utils.extensions.setBackStackData
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment :
    BaseFragment<SignInBinding, SignInViewModel>(R.layout.fr_signin) {

    override val viewModel by viewModel<SignInViewModel>()

    override fun setupViews() {
        setupObservers()
        handleOnBackPressed()
    }

    private fun setupObservers() {
        viewModel.togglePhoneEmailCredentialsText.observe(viewLifecycleOwner) {}
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is SignInViewModel.Command.ReturnToOnboarding ->
                    navController?.setBackStackData(
                        RESTART_ONBOARDING_STEPS,
                        true
                    )
                is SignInViewModel.Command.ToggleTextVisibilityAnimated ->
                    binding?.clTogglePhoneEmail?.setAlphaAnimationVisibility(it.alphaAnimationConfig)
            }
        }
        viewModel.password.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.email.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.phoneNumber.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
    }

    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.onReturnToOnboarding()
                }
            }
        )
    }
}
