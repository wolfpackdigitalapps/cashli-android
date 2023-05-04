package com.wolfpackdigital.cashli.presentation.linkBank.ineligible

import com.wolfpackdigital.cashli.IneligibleInformativeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class IneligibleInformativeFragment :
    BaseFragment<IneligibleInformativeBinding, IneligibleInformativeViewModel>(
        R.layout.fr_ineligible_informative
    ) {

    override val viewModel by viewModel<IneligibleInformativeViewModel>()

    override fun setupViews() {}
}
