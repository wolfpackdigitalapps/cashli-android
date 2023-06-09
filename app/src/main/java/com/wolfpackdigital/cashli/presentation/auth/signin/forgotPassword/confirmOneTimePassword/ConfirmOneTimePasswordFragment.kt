package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword

import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.ConfirmOneTimePasswordBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ConfirmOneTimePasswordFragment :
    BaseFragment<ConfirmOneTimePasswordBinding, ConfirmOneTimePasswordViewModel>(
        R.layout.fr_confirm_one_time_password
    ) {

    private val navArgs by navArgs<ConfirmOneTimePasswordFragmentArgs>()
    override val viewModel by viewModel<ConfirmOneTimePasswordViewModel> {
        parametersOf(
            navArgs.phoneNumberOrEmail,
            CodeReceivedViaType.values().find { it.ordinal == navArgs.codeReceivedViaType },
            navArgs.fromEditProfile
        )
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.verificationCode.observe(viewLifecycleOwner) { viewModel.clearInvalidCodeError() }
    }
}
