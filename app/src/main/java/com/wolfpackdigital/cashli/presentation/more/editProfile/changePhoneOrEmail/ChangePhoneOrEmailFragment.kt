package com.wolfpackdigital.cashli.presentation.more.editProfile.changePhoneOrEmail

import androidx.navigation.fragment.navArgs
import com.wolfpackdigital.cashli.ChangePhoneOrEmailBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ChangePhoneOrEmailFragment : BaseFragment<ChangePhoneOrEmailBinding, ChangePhoneOrEmailViewModel>(
    R.layout.fr_change_phone_email) {

    private val navArgs by navArgs<ChangePhoneOrEmailFragmentArgs>()
    override val viewModel by viewModel<ChangePhoneOrEmailViewModel>() {
        parametersOf(
            navArgs.changePhoneOrEmail
        )
    }

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.phoneOrEmail.observe(viewLifecycleOwner) { viewModel.clearPhoneOrEmailError() }
    }
}