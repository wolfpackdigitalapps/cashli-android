package com.wolfpackdigital.cashli.presentation.home

import com.wolfpackdigital.cashli.HomeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment :
    BaseFragment<HomeBinding, HomeViewModel>(R.layout.fr_home) {

    override val viewModel by viewModel<HomeViewModel>()

    override fun setupViews() {
    }
}
