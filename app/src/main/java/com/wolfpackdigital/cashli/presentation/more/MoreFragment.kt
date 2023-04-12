package com.wolfpackdigital.cashli.presentation.more

import com.wolfpackdigital.cashli.MoreBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoreFragment :
    BaseFragment<MoreBinding, MoreViewModel>(R.layout.fr_more) {

    override val viewModel by viewModel<MoreViewModel>()

    override fun setupViews() {
    }
}
