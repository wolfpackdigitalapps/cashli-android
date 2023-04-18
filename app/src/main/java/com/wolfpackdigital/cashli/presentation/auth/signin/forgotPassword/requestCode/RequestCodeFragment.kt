package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.RequestCodeBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.setAlphaAnimationVisibility
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestCodeFragment :
    BaseFragment<RequestCodeBinding, RequestCodeViewModel>(R.layout.fr_request_code) {

    override val viewModel by viewModel<RequestCodeViewModel>()
    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.togglePhoneEmailCredentialsText.observe(viewLifecycleOwner) {}
        viewModel.email.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.phoneNumber.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.cmd.observe(viewLifecycleOwner) { cmd ->
            when (cmd) {
                is RequestCodeViewModel.Command.ToggleTextVisibilityAnimated ->
                    binding?.clTogglePhoneEmail?.setAlphaAnimationVisibility(cmd.alphaAnimationConfig)
            }
        }
    }
}
