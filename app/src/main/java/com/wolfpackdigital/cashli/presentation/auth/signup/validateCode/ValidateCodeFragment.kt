package com.wolfpackdigital.cashli.presentation.auth.signup.validateCode

import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.ValidateCodeBinding
import com.wolfpackdigital.cashli.presentation.auth.signup.SignUpSharedViewModel
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.showSMSApp
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ValidateCodeFragment :
    BaseFragment<ValidateCodeBinding, ValidateCodeViewModel>(R.layout.fr_validate_code) {

    private val signUpSharedViewModel: SignUpSharedViewModel by activityViewModels()

    private val navArgs by navArgs<ValidateCodeFragmentArgs>()
    override val viewModel by viewModel<ValidateCodeViewModel>() {
        val identifier = when (navArgs.codeReceivedViaType) {
            CodeReceivedViaType.SMS -> signUpSharedViewModel.getUserPhoneNumber()
            CodeReceivedViaType.EMAIL -> signUpSharedViewModel.getUserEmail()
        }
        parametersOf(
            identifier,
            navArgs.codeReceivedViaType
        )
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.verificationCode.observe(viewLifecycleOwner) { viewModel.clearInvalidCodeError() }
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                ValidateCodeViewModel.Command.GetSupport -> activity?.showSMSApp()
                is ValidateCodeViewModel.Command.SavePhoneNumberToken ->
                    signUpSharedViewModel.updatePhoneNumberToken(it.phoneNumberToken)
                is ValidateCodeViewModel.Command.SaveEmailToken ->
                    signUpSharedViewModel.updateEmailToken(it.emailToken)
            }
        }
    }
}
