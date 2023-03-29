package com.wolfpackdigital.cashli.presentation.auth.signup

import com.wolfpackdigital.cashli.InformativeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class InformativeFragment :
    BaseFragment<InformativeBinding, InformativeViewModel>(R.layout.fr_informative) {

    override val viewModel by viewModel<InformativeViewModel>()

    override fun setupViews() {
    }
}
