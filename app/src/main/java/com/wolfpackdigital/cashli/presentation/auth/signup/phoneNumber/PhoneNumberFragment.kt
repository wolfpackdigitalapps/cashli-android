package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import androidx.fragment.app.activityViewModels
import com.wolfpackdigital.cashli.PhoneNumberBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.auth.signup.SignUpSharedViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhoneNumberFragment :
    BaseFragment<PhoneNumberBinding, PhoneNumberViewModel>(R.layout.fr_phone_number) {

    private val signUpSharedViewModel: SignUpSharedViewModel by activityViewModels()

    override val viewModel by viewModel<PhoneNumberViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        signUpSharedViewModel.clearSignUpData()
        signUpSharedViewModel.initSignUpData()
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is PhoneNumberViewModel.Command.SavePhoneNumber ->
                    signUpSharedViewModel.updatePhoneNumber(it.phoneNumber)
            }
        }
    }
}
