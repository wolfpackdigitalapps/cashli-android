package com.wolfpackdigital.cashli.presentation.auth.signup.createProfile

import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import com.wolfpackdigital.cashli.CreateProfileBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.auth.signup.SignUpSharedViewModel
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateProfileFragment :
    BaseFragment<CreateProfileBinding, CreateProfileViewModel>(R.layout.fr_create_profile) {

    private val signUpSharedViewModel: SignUpSharedViewModel by activityViewModels()

    override val viewModel by viewModel<CreateProfileViewModel>()

    override fun setupViews() {
        handleOnBackPressed()
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.zipCode.observe(viewLifecycleOwner) { viewModel.clearZipCodeError() }
        viewModel.email.observe(viewLifecycleOwner) { viewModel.clearEmailError() }
        viewModel.cityAndState.observe(viewLifecycleOwner) { viewModel.clearCityAndStateError() }
        viewModel.street.observe(viewLifecycleOwner) { viewModel.clearStreetError() }
        viewModel.firstName.observe(viewLifecycleOwner) { viewModel.clearFirstNameError() }
        viewModel.lastName.observe(viewLifecycleOwner) { viewModel.clearLastNameError() }
        viewModel.cmd.observe(viewLifecycleOwner) {
            when (it) {
                is CreateProfileViewModel.Command.SaveUserProfile ->
                    signUpSharedViewModel.updateProfile(it.temporaryUserProfileRequest)
            }
        }
    }

    private fun handleOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner) {}
    }
}
