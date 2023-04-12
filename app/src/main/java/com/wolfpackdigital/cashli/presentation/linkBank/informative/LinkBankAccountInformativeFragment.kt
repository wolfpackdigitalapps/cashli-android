package com.wolfpackdigital.cashli.presentation.linkBank.informative

import com.wolfpackdigital.cashli.LinkBankAccountInformativeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class LinkBankAccountInformativeFragment :
    BaseFragment<LinkBankAccountInformativeBinding, LinkBankAccountInformativeViewModel>(
        R.layout.fr_link_bank_account_informative
    ) {

    override val viewModel by viewModel<LinkBankAccountInformativeViewModel>()

    override fun setupViews() {
    }
}
