package com.wolfpackdigital.cashli.domain.entities.claimCash

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.wolfpackdigital.cashli.R

@Suppress("LongParameterList")
enum class DeliveryMethod(
    @StringRes val methodName: Int,
    @StringRes val description: Int,
    @StringRes val secondaryDescription: Int? = null,
    @StringRes val disclaimer: Int,
    @DrawableRes val background: Int,
    @DrawableRes val icon: Int,
    @ColorRes val tint: Int,
    @StringRes val tooltipText: Int? = null
) {
    REGULAR(
        methodName = R.string.delivery_method_regular,
        description = R.string.delivery_method_regular_description,
        secondaryDescription = null,
        disclaimer = R.string.delivery_method_regular_disclaimer,
        background = R.drawable.bg_regular_item,
        icon = R.drawable.ic_regular_delivery,
        tint = R.color.colorGreen71,
    ),
    EXPRESS_WITHIN_MINUTES(
        methodName = R.string.delivery_method_express,
        description = R.string.delivery_method_express_within_minutes,
        secondaryDescription = R.string.delivery_method_express_secondary_description,
        disclaimer = R.string.delivery_method_express_disclaimer,
        background = R.drawable.bg_express_item,
        icon = R.drawable.ic_express_delivery,
        tint = R.color.colorAccent
    ),
    EXPRESS_SEVERAL_HOURS(
        methodName = R.string.delivery_method_express,
        description = R.string.delivery_method_express_several_hours,
        disclaimer = R.string.delivery_method_regular_disclaimer,
        background = R.drawable.bg_express_item,
        icon = R.drawable.ic_express_delivery,
        tint = R.color.colorAccent,
        tooltipText = R.string.pending
    ),
    EXPRESS_WITHIN_20_HOURS(
        methodName = R.string.delivery_method_express,
        description = R.string.delivery_method_express_within_minutes,
        disclaimer = R.string.delivery_method_regular_disclaimer,
        background = R.drawable.bg_express_item,
        icon = R.drawable.ic_express_delivery,
        tint = R.color.colorAccent,
        tooltipText = R.string.pending
    )
}
