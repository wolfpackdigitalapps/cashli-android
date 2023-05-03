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
        viewModel.amountPerc.observe(viewLifecycleOwner, ::moveLabel)
        viewModel.deliveryMethods.observe(viewLifecycleOwner, adapter::submitList)
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when (command) {
                is ClaimCashViewModel.Command.TransitionToStart -> transitionToStart()
            }
        }
    }

    private fun setupRv() {
        binding?.deliveryMethodRv?.apply {
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
        binding?.cashAmountMl?.transitionToStart()
    }

    private fun moveLabel(perc: Int) {
        binding?.cashAmountMl?.getConstraintSet(R.id.end)?.let {
            binding?.cashAmountSb?.thumb?.bounds?.centerX()
            val seekBar = binding?.cashAmountSb ?: return
            val progressValue = perc * (seekBar.width - 2 * seekBar.thumbOffset) / seekBar.max
            it.setHorizontalBias(
                R.id.cash_amount_label_tv,
                (progressValue / PROGRESS_DIVISION_FACTOR).coerceIn(MIN_BIAS, MAX_BIAS)
            )
            binding?.cashAmountLabelTv?.apply {
                this.layoutParams = layoutParams
                text = getString(R.string.dollar_amount, perc)
            }
        }
    }

    private companion object {
        const val PROGRESS_DIVISION_FACTOR = 1000f
        const val MIN_BIAS = 0.05f
        const val MAX_BIAS = 0.95f
    }
}
