package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.StringRes

data class GenericWarningInfo(
    @StringRes val warningTextId: Int,
    val spanActions: List<TextSpanAction>?
)
