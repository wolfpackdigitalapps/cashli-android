package com.wolfpackdigital.cashli.presentation.auth.signup.validateCode

import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.ValidateCodeBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.showDialer
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ValidateCodeFragment :
    BaseFragment<ValidateCodeBinding, ValidateCodeViewModel>(R.layout.fr_validate_code) {

    private val navArgs by navArgs<ValidateCodeFragmentArgs>()
    override val viewModel by viewModel<ValidateCodeViewModel>() {
        parametersOf(navArgs.codeReceivedViaType)
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.verificationCode.observe(viewLifecycleOwner) { viewModel.clearInvalidCodeError() }
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                ValidateCodeViewModel.Command.GetSupport -> activity?.showDialer()
            }
        }
    }
}
