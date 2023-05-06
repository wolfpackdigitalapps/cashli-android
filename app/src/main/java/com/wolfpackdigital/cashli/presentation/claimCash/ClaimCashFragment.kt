package com.wolfpackdigital.cashli.presentation.claimCash

import androidx.recyclerview.widget.GridLayoutManager
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.ClaimCashFragmentBinding
import com.wolfpackdigital.cashli.presentation.claimCash.adapter.DeliveryMethodAdapter
import com.wolfpackdigital.cashli.presentation.claimCash.adapter.DeliveryMethodItemDecoration
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val COLUMN_COUNT = 2

class ClaimCashFragment : BaseFragment<ClaimCashFragmentBinding, ClaimCashViewModel>(
    layoutResource = R.layout.fr_claim_cash
) {

    override val viewModel by viewModel<ClaimCashViewModel>()

    private val adapter by lazy {
        DeliveryMethodAdapter(viewModel::onDeliveryMethodSelected)
    }

    override fun setupViews() {
        setupRv()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.amountPerc.observe(viewLifecycleOwner, ::moveValueText)
        viewModel.deliveryMethods.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when (command) {
                is ClaimCashViewModel.Command.TransitionToStart -> transitionToStart()
            }
        }
    }

    private fun setupRv() {
        binding?.rvDeliveryMethod?.apply {
            adapter = this@ClaimCashFragment.adapter
            itemAnimator = null
            layoutManager = GridLayoutManager(this.context, COLUMN_COUNT).apply {
                addItemDecoration(
                    DeliveryMethodItemDecoration(
                        spanCount = COLUMN_COUNT,
                        spacing = context.resources.getDimensionPixelSize(R.dimen.dimen_12dp),
                        includeEdge = true,
                        headerNum = 0
                    )
                )
            }
        }
    }

    private fun transitionToStart() {
        binding?.mlCashAmount?.transitionToStart()
    }

    private fun moveValueText(perc: Int) {
        binding?.sbCashAmount?.let { sb ->
            binding?.tvCashAmountLabel?.apply {
                text = getString(R.string.dollar_amount, perc)
                x = sb.thumb.bounds.exactCenterX() - sb.thumbOffset
            }
        }
    }
}