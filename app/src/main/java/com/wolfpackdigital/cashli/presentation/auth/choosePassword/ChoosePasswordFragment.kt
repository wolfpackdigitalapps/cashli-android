package com.wolfpackdigital.cashli.presentation.auth.choosePassword

import androidx.activity.OnBackPressedCallback
import com.wolfpackdigital.cashli.ChoosePasswordBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoosePasswordFragment :
    BaseFragment<ChoosePasswordBinding, ChoosePasswordViewModel>(R.layout.fr_choose_password) {

    override val viewModel by viewModel<ChoosePasswordViewModel>()

    override fun setupViews() {
        handleOnBackPressed()
    }

    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                }
            }
        )
    }
}
