package com.wolfpackdigital.cashli.presentation.entities

import androidx.annotation.StringRes
import com.wolfpackdigital.cashli.R

enum class HeaderItem(
    @StringRes val title: Int? = null
) {
    ADVANCE_HISTORY(
        title = R.string.advance_history
    )
}
