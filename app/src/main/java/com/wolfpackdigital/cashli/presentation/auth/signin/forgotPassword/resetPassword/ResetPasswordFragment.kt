package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.resetPassword

import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.ResetPasswordBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ResetPasswordFragment :
    BaseFragment<ResetPasswordBinding, ResetPasswordViewModel>(R.layout.fr_reset_password) {

    private val navArgs by navArgs<ResetPasswordFragmentArgs>()
    override val viewModel by viewModel<ResetPasswordViewModel> {
        parametersOf(
            navArgs.token
        )
    }

    override fun setupViews() {
        handleOnBackPressed()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.password.observe(viewLifecycleOwner) { viewModel.clearPasswordError() }
        viewModel.confirmPassword.observe(viewLifecycleOwner) { viewModel.clearPasswordError() }
    }

    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    viewModel.backToRequestCode()
                }
            }
        )
    }
}
