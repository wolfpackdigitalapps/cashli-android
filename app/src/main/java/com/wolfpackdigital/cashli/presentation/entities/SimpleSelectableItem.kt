package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.StringRes

data class SimpleSelectableItem(
    @StringRes
    val value: Int,
    var isChecked: Boolean = false
)
