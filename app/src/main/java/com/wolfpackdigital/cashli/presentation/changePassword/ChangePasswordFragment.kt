package com.wolfpackdigital.cashli.presentation.changePassword

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ChangePasswordFragmentBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChangePasswordFragment : BaseFragment<ChangePasswordFragmentBinding, ChangePasswordViewModel>(
    R.layout.fr_change_password
) {

    override val viewModel by viewModel<ChangePasswordViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.currentPassword.observe(viewLifecycleOwner) { viewModel.clearCurrentPasswordError() }
        viewModel.newPassword.observe(viewLifecycleOwner) { viewModel.clearNewPasswordError() }
        viewModel.confirmNewPassword.observe(viewLifecycleOwner) { viewModel.clearConfirmNewPasswordError() }
    }
}
