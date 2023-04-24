package com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword

import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.wolfpackdigital.cashli.ChoosePasswordBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.auth.signup.SignUpSharedViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoosePasswordFragment :
    BaseFragment<ChoosePasswordBinding, ChoosePasswordViewModel>(R.layout.fr_choose_password) {

    private val signUpSharedViewModel: SignUpSharedViewModel by activityViewModels()

    override val viewModel by viewModel<ChoosePasswordViewModel>()

    override fun setupViews() {
        handleOnBackPressed()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.password.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.confirmPassword.observe(viewLifecycleOwner) { viewModel.clearFieldsError() }
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is ChoosePasswordViewModel.Command.SavePassword ->
                    signUpSharedViewModel.updatePassword(it.password)
                ChoosePasswordViewModel.Command.CreateUserProfileRequest -> {
                    val profileRequest = signUpSharedViewModel.createUserProfileRequest()
                    viewModel.registerNewUser(profileRequest)
                }
                ChoosePasswordViewModel.Command.ClearSignUpData ->
                    signUpSharedViewModel.clearSignUpData()
            }
        }
    }

    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {}
    }
}
