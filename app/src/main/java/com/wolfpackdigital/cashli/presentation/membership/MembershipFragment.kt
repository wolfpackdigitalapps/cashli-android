package com.wolfpackdigital.cashli.presentation.membership

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.MembershipFragmentBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MembershipFragment : BaseFragment<MembershipFragmentBinding, MembershipViewModel>(
    layoutResource = R.layout.fr_membership
) {

    override val viewModel by viewModel<MembershipViewModel>()

    override fun setupViews() {
    }
}
