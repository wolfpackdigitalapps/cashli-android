package com.wolfpackdigital.cashli.presentation.main.splash

import androidx.lifecycle.lifecycleScope
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.SplashScreenBinding
import com.wolfpackdigital.cashli.shared.base.BaseFragment
import com.wolfpackdigital.cashli.shared.utils.extensions.navController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

const val DELAY = 1500L

@Suppress("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<SplashScreenBinding, SplashScreenViewModel>(R.layout.fr_splash_screen) {
    override val viewModel by viewModel<SplashScreenViewModel>()

    override fun setupViews() {
        lifecycleScope.launch {
            delay(DELAY)
            navController?.navigate(SplashScreenFragmentDirections.actionSplashScreenFragmentToListFragment())
        }
    }
}
