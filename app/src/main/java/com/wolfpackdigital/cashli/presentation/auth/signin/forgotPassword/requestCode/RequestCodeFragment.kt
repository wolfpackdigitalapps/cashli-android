package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.RequestCodeBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class RequestCodeFragment :
    BaseFragment<RequestCodeBinding, RequestCodeViewModel>(R.layout.fr_request_code) {

    override val viewModel by viewModel<RequestCodeViewModel>()
    override fun setupViews() {}
}