package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.ColorRes

data class TextSpanAction(
    val actionKey: String,
    @ColorRes val spanTextColor: Int,
    val isSpanTextUnderlined: Boolean = false,
    val isSpanTextBold: Boolean = true,
    val action: () -> Unit = {}
)
