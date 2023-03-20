package com.wolfpackdigital.baseproject.presentation.main.list

import com.wolfpackdigital.baseproject.ListBinding
import com.wolfpackdigital.baseproject.R
import com.wolfpackdigital.baseproject.shared.base.BaseFragment
import com.wolfpackdigital.baseproject.shared.base.BaseViewModel
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
