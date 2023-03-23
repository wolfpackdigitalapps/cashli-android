package com.wolfpackdigital.cashli.presentation.main.language

import com.wolfpackdigital.cashli.ChooseLanguageBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChooseLanguageFragment: BaseFragment<ChooseLanguageBinding, ChooseLanguageViewModel>(R.layout.fr_choose_language) {

    override val viewModel by viewModel<ChooseLanguageViewModel>()

    override fun setupViews() {
        TODO("Not yet implemented")
    }

}