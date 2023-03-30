package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Toolbar(
    @StringRes val titleId: Int? = null,
    @DrawableRes val titleLogoId: Int? = null,
    val isBackVisible: Boolean = true,
    val currentStep: Int = 0,
    val isStepCounterVisible: Boolean = false,
    val onBack: () -> Unit = {}
) {
    fun onBackPressed() = onBack()
}
