package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.StringRes

data class HomeGenericWarningInfo(
    @StringRes val warningTextId: Int,
    val spanActions: List<TextSpanAction>
)