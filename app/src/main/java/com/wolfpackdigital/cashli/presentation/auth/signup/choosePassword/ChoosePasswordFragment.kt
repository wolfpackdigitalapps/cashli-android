package com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword

import androidx.activity.addCallback
import com.wolfpackdigital.cashli.ChoosePasswordBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoosePasswordFragment :
    BaseFragment<ChoosePasswordBinding, ChoosePasswordViewModel>(R.layout.fr_choose_password) {

    override val viewModel by viewModel<ChoosePasswordViewModel>()

    override fun setupViews() {
        handleOnBackPressed()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.password.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.confirmPassword.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
    }
    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {}
    }
}
