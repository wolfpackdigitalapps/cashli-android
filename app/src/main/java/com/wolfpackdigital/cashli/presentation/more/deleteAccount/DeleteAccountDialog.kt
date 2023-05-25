package com.wolfpackdigital.cashli.presentation.more.deleteAccount

import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.DeleteAccountDialogBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DeleteAccountDialog :
    BaseDialogFragment<DeleteAccountDialogBinding, DeleteAccountViewModel>(
        R.layout.dialog_delete_account
    ) {

    private val navArgs by navArgs<DeleteAccountDialogArgs>()

    override val viewModel by viewModel<DeleteAccountViewModel> {
        parametersOf(
            navArgs.deleteAccountArgs
        )
    }

    private val deleteAccountReasonsAdapter by lazy {
        DeleteAccountReasonsAdapter(viewModel::onCloseAccountReasonSelected)
    }

    override fun setupViews() {
        setupRv()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.closeAccountReasons.observe(viewLifecycleOwner, deleteAccountReasonsAdapter::submitList)
    }

    private fun setupRv() {
        dialogBinding?.rvReasons?.apply {
            adapter = deleteAccountReasonsAdapter
            itemAnimator = null
        }
    }

    override fun getTheme() = R.style.RoundedCornersDialog
}
