package com.wolfpackdigital.cashli.presentation.help

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.databinding.HelpFragmentBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val MAILTO_URI = "mailto:"

class HelpFragment : BaseFragment<HelpFragmentBinding, HelpViewModel>(R.layout.fr_help) {

    override val viewModel by viewModel<HelpViewModel>()

    override fun setupViews() {
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.cmd.observe(viewLifecycleOwner) { command ->
            when (command) {
                is HelpViewModel.Command.OpenEmailApp -> context?.launchEmailIntent(command.email)
            }
        }
    }

    private fun Context.launchEmailIntent(address: String) {
        with(Intent(Intent.ACTION_SENDTO)) {
            data = Uri.parse(MAILTO_URI)
            putExtra(Intent.EXTRA_EMAIL, arrayOf(address))

            try {
                startActivity(this)
            } catch (E: ActivityNotFoundException) {
                toast(R.string.no_mail_app_found)
            }
        }
    }
}
