package com.wolfpackdigital.cashli.presentation.auth.signup.createProfile

import com.wolfpackdigital.cashli.CreateProfileBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProfileFragment: BaseFragment<CreateProfileBinding, CreateProfileViewModel>(R.layout.fr_create_profile) {

    override val viewModel by viewModel<CreateProfileViewModel>()

    override fun setupViews() {}
}