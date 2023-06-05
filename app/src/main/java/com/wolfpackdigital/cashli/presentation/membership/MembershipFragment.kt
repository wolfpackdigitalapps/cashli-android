package com.wolfpackdigital.cashli.presentation.membership

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.MembershipFragmentBinding
import com.wolfpackdigital.cashli.presentation.entities.HeaderItem
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.Constants.SCROLL_TO_TOP
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
import com.wolfpackdigital.cashli.shared.utils.extensions.canScrollBothDirections
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewBottom
import com.wolfpackdigital.cashli.shared.utils.extensions.reachedViewTop
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MembershipFragment : BaseFragment<MembershipFragmentBinding, MembershipViewModel>(
    layoutResource = R.layout.fr_membership
) {

    override val viewModel by viewModel<MembershipViewModel>()

    private val membershipAdapter: MembershipAdapter by lazy {
        MembershipAdapter()
    }
    private val advanceHistoryHeaderAdapter: HeaderAdapter by lazy {
        HeaderAdapter()
    }
    private val cashAdvanceHistoryAdapter: CashAdvanceHistoryAdapter by lazy {
        CashAdvanceHistoryAdapter()
    }
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(
            membershipAdapter,
            advanceHistoryHeaderAdapter,
            cashAdvanceHistoryAdapter
        )
    }

    override fun setupViews() {
        binding?.rvMembershipAndAdvances?.apply {
            adapter = concatAdapter
        }
        binding?.fabScrollToTop?.setOnClickDebounced {
            binding?.rvMembershipAndAdvances?.smoothScrollToPosition(SCROLL_TO_TOP)
        }
        handleTransactionsListScroll()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.membershipItem.observe(viewLifecycleOwner) { membershipAdvanceItem ->
            membershipAdapter.submitList(
                membershipAdvanceItem?.let { item -> listOf(item) } ?: emptyList()
            )
        }
        lifecycleScope.launch {
            viewModel.cashAdvanceHistoryFlow.collectLatest { pagingData ->
                cashAdvanceHistoryAdapter.submitData(pagingData)
            }
        }
        lifecycleScope.launch {
            cashAdvanceHistoryAdapter.loadStateFlow.distinctUntilChangedBy {
                it.refresh
            }.collect {
                if (cashAdvanceHistoryAdapter.snapshot().isEmpty())
                    advanceHistoryHeaderAdapter.submitList(emptyList())
                else
                    advanceHistoryHeaderAdapter.submitList(listOf(HeaderItem.ADVANCE_HISTORY))
            }
        }
    }

    @Suppress("ComplexCondition")
    private fun handleTransactionsListScroll() {
        binding?.rvMembershipAndAdvances?.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (cashAdvanceHistoryAdapter.itemCount > 0 && binding?.fabScrollToTop?.isShown == false &&
                        (
                            binding?.rvMembershipAndAdvances?.canScrollBothDirections() == true ||
                                binding?.rvMembershipAndAdvances?.reachedViewBottom() == true
                            )
                    ) {
                        binding?.fabScrollToTop?.show()
                    } else if (binding?.rvMembershipAndAdvances?.reachedViewTop() == true &&
                        binding?.fabScrollToTop?.isShown == true
                    ) {
                        binding?.fabScrollToTop?.hide()
                    }
                }
            })
    }
}
