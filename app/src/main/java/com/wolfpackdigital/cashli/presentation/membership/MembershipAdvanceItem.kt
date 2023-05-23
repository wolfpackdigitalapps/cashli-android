package com.wolfpackdigital.cashli.presentation.membership

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wolfpackdigital.cashli.R

@Suppress("LongParameterList")
sealed class MembershipAdvanceItem(
    val itemType: MembershipAdvanceItemType,
    val topValue: String,
    val middleValue: String,
    val bottomValue: String,
    @StringRes val status: Int,
    @DrawableRes val statusDrawableRes: Int,
    @ColorRes val statusTint: Int = R.color.colorWhite,
    @DrawableRes val topTint: Int? = null
) {

    data class MembershipActiveItem(
        private val topText: String,
        private val middleText: String,
        private val bottomText: String
    ) : MembershipAdvanceItem(
        itemType = MembershipAdvanceItemType.MEMBERSHIP,
        topValue = topText,
        middleValue = middleText,
        bottomValue = bottomText,
        status = R.string.membership_status_active,
        statusDrawableRes = R.drawable.ic_check,
        statusTint = R.color.colorGreen71
    )

    data class MembershipPausedItem(
        private val topText: String,
        private val middleText: String,
        private val bottomText: String
    ) : MembershipAdvanceItem(
        itemType = MembershipAdvanceItemType.MEMBERSHIP,
        topValue = topText,
        middleValue = middleText,
        bottomValue = bottomText,
        status = R.string.membership_status_paused,
        statusDrawableRes = R.drawable.ic_paused,
        statusTint = R.color.colorRed62
    )

    data class AdvancePaidItem(
        private val topText: String,
        private val middleText: String,
        private val bottomText: String
    ) : MembershipAdvanceItem(
        itemType = MembershipAdvanceItemType.CASH_ADVANCE,
        topValue = topText,
        middleValue = middleText,
        bottomValue = bottomText,
        status = R.string.cash_advance_paid,
        statusDrawableRes = R.drawable.ic_check,
        topTint = R.color.colorGreen71
    )

    data class AdvanceOverdueItem(
        private val topText: String,
        private val middleText: String,
        private val bottomText: String
    ) : MembershipAdvanceItem(
        itemType = MembershipAdvanceItemType.CASH_ADVANCE,
        topValue = topText,
        middleValue = middleText,
        bottomValue = bottomText,
        status = R.string.cash_advance_overdue,
        statusDrawableRes = R.drawable.ic_alert,
        topTint = R.color.colorAccent
    )
}
