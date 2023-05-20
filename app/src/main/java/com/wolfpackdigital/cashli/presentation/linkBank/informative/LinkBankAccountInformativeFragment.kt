package com.wolfpackdigital.cashli.presentation.linkBank.informative

import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.LinkBankAccountInformativeBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.plaid.LinkPlaidAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import com.wolfpackdigital.cashli.shared.utils.extensions.setBackStackData
import org.koin.androidx.viewmodel.ext.android.viewModel

class LinkBankAccountInformativeFragment :
    BaseFragment<LinkBankAccountInformativeBinding, LinkBankAccountInformativeViewModel>(
        R.layout.fr_link_bank_account_informative
    ) {

    override val viewModel by viewModel<LinkBankAccountInformativeViewModel>()

    private val linkAccountToPlaidLauncher =
        registerForActivityResult(OpenPlaidLink()) { linkResult ->
            when (linkResult) {
                is LinkSuccess -> viewModel.onSuccessLinkingBankAccount(linkResult)
                is LinkExit -> viewModel.onFailLinkingBankAccount(linkResult)
            }
        }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is LinkPlaidAccountViewModel.Command.StartLinkingBackAccount -> {
                    linkAccountToPlaidLauncher.launch(it.linkTokenConfiguration)
                }

                LinkPlaidAccountViewModel.Command.RefreshUserDataNeeded ->
                    navController?.setBackStackData(
                        Constants.REFRESH_USER_DATA,
                        true
                    )
            }
        }
    }
}
