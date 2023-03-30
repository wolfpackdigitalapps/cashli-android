package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import com.wolfpackdigital.cashli.PhoneNumberBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class PhoneNumberFragment : BaseFragment<PhoneNumberBinding, PhoneNumberViewModel>(R.layout.fr_phone_number) {

    override val viewModel by viewModel<PhoneNumberViewModel>()

    override fun setupViews() {}
}
