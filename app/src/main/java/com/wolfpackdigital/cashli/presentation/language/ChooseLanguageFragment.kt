package com.wolfpackdigital.cashli.presentation.language

import com.wolfpackdigital.cashli.ChooseLanguageBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.yariksoffice.lingver.Lingver
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseLanguageFragment :
    BaseFragment<ChooseLanguageBinding, ChooseLanguageViewModel>(R.layout.fr_choose_language) {

    override val viewModel by viewModel<ChooseLanguageViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is ChooseLanguageViewModel.Command.ChangeAppLocale ->
                    context?.let { ctx -> Lingver.getInstance().setLocale(ctx, it.language) }
            }
        }
    }
}
