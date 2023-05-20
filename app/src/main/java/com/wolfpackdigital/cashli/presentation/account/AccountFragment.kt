package com.wolfpackdigital.cashli.presentation.account

import com.plaid.link.OpenPlaidLink
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.AccountBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.plaid.LinkPlaidAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class AccountFragment :
    BaseFragment<AccountBinding, AccountViewModel>(R.layout.fr_account) {

    override val viewModel by viewModel<AccountViewModel>()

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
                    viewModel.getUserProfile()
            }
        }
    }
}
