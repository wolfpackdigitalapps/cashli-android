package com.wolfpackdigital.baseproject.presentation.main.splash

import androidx.lifecycle.lifecycleScope
import com.wolfpackdigital.baseproject.R
import com.wolfpackdigital.baseproject.SplashScreenBinding
import com.wolfpackdigital.baseproject.shared.base.BaseFragment
import com.wolfpackdigital.baseproject.shared.utils.extensions.navController
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
