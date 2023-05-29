package com.wolfpackdigital.cashli.presentation.membership

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.wolfpackdigital.cashli.R

enum class MembershipAdvanceItemType(
    @StringRes val topLabel: Int,
    @StringRes val middleLabel: Int,
    @StringRes val bottomLabel: Int,
    @StringRes val title: Int? = null,
    @ColorRes val bottomPartTint: Int? = null
) {
    MEMBERSHIP(
        title = R.string.membership_status,
        topLabel = R.string.membership_started,
        middleLabel = R.string.membership_renewal,
        bottomLabel = R.string.membership_amount,
    ),
    CASH_ADVANCE(
        topLabel = R.string.cash_advance_amount,
        middleLabel = R.string.cash_advance_due_date,
        bottomLabel = R.string.cash_advance_paid_date,
        bottomPartTint = R.color.colorWhiteF5,
    )
}
