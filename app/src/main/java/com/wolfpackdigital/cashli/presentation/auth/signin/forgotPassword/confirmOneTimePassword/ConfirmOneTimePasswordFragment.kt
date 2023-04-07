package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword

import com.wolfpackdigital.cashli.ConfirmOneTimePasswordBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ConfirmOneTimePasswordFragment :
    BaseFragment<ConfirmOneTimePasswordBinding, ConfirmOneTimePasswordViewModel>(
        R.layout.fr_confirm_one_time_password
    ) {

    override val viewModel by viewModel<ConfirmOneTimePasswordViewModel>()

    override fun setupViews() {}
}