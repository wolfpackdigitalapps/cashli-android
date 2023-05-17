package com.wolfpackdigital.cashli.presentation.more.settings

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.SettingsBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment<SettingsBinding, SettingsViewModel>(R.layout.fr_settings) {

    override val viewModel by viewModel<SettingsViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {}
}
