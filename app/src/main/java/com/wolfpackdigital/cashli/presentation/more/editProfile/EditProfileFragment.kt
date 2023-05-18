package com.wolfpackdigital.cashli.presentation.more.editProfile

import com.wolfpackdigital.cashli.EditProfileBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment :
    BaseFragment<EditProfileBinding, EditProfileViewModel>(R.layout.fr_edit_profile) {

    override val viewModel by viewModel<EditProfileViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.newFirstName.observe(viewLifecycleOwner) { viewModel.clearFirstNameError() }
        viewModel.newLastName.observe(viewLifecycleOwner) { viewModel.clearLastNameError() }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setUpdatedData()
    }
}
