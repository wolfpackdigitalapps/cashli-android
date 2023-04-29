package com.wolfpackdigital.cashli.presentation.claimCash

import androidx.constraintlayout.widget.ConstraintLayout
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ClaimCashFragmentBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClaimCashFragment : BaseFragment<ClaimCashFragmentBinding, ClaimCashViewModel>(
    layoutResource = R.layout.fr_claim_cash
) {

    override val viewModel by viewModel<ClaimCashViewModel>()


    override fun setupViews() {
        setupObservers()
//        setupThumbTracking()
    }

    private fun setupObservers() {
        viewModel.amountPerc.observe(viewLifecycleOwner, ::moveLabel)
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when(command) {
                is ClaimCashViewModel.Command.TransitionToStart -> transitionToStart()
            }

        }
    }

    private fun transitionToStart() {
        binding?.cashAmountMl?.transitionToStart()
    }

    private fun moveLabel(perc: Int) {
        val layoutParams =
            (binding?.cashAmountLabelTv?.layoutParams as? ConstraintLayout.LayoutParams)?.apply {
                horizontalBias = perc.toFloat() / 100
            }
        binding?.cashAmountLabelTv?.apply {
            this.layoutParams = layoutParams
            text = getString(R.string.dollar_amount, perc)
        }
    }
}