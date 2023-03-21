package com.wolfpackdigital.cashli.presentation.main.list

import com.wolfpackdigital.cashli.ListBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListFragment : BaseFragment<ListBinding, BaseViewModel>(R.layout.fr_list) {

    override val viewModel by viewModel<ListViewModel>()

    private val adapter by lazy {
        MockListAdapter()
    }
    override fun setupViews() {
        binding?.rvMock?.adapter = adapter
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.mockList.observe(viewLifecycleOwner, adapter::submitList)
    }
}
