package com.wolfpackdigital.cashli.presentation.account

import com.wolfpackdigital.cashli.AccountBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment :
    BaseFragment<AccountBinding, AccountViewModel>(R.layout.fr_account) {

    override val viewModel by viewModel<AccountViewModel>()

    override fun setupViews() {
    }
}
