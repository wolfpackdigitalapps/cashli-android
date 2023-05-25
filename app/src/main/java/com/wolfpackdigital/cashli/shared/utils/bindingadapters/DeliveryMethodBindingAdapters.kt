package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.IconGravity
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.claimCash.DeliveryMethod
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet

const val ARROW_MIDDLE_POSITION = 0.5f
private const val DISABLED_ALPHA = 0.5f

@BindingAdapter("deliveryMethod", "userProfile")
fun TextView.setDisclaimerForDeliveryMethod(
    deliveryMethod: DeliveryMethod?,
    userProfile: UserProfile?
) {
    safeLet(deliveryMethod, userProfile?.bankAccount) { type, bankAccount ->
        when (type) {
            DeliveryMethod.EXPRESS_WITHIN_MINUTES -> {
                setText(type.disclaimer)
            }

            else -> {
                text = context.getString(
                    type.disclaimer,
                    bankAccount.institutionName,
                    bankAccount.accountNumberMask
                )
            }
        }
    }
}

@BindingAdapter("disabled")
fun ConstraintLayout.setIsDisabled(isDisabled: Boolean?) {
    isDisabled?.let {
        alpha = if (it) DISABLED_ALPHA else 1f
        isClickable = !it
    }
}

@BindingAdapter(value = ["tooltip_text", "requirement_image", "onClick"], requireAll = false)
fun ImageView.setCustomTextBalloon(
    @StringRes tooltipText: Int?,
    @DrawableRes image: Int?,
    onClick: (() -> Unit)?
) {
    tooltipText?.let {
        hide(false)
        setOnClickListener { view ->
            val builder = Balloon.Builder(context)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setPaddingTopResource(R.dimen.dimen_16dp)
                .setPaddingBottomResource(R.dimen.dimen_16dp)
                .setPaddingRightResource(R.dimen.dimen_12dp)
                .setPaddingLeftResource(R.dimen.dimen_12dp)
                .setIconGravity(IconGravity.START)
                .setIconSizeResource(R.dimen.dimen_14dp)
                .setCornerRadiusResource(R.dimen.dimen_8dp)
                .setTextColorResource(R.color.colorPrimaryDark)
                .setTextSizeResource(R.dimen.size_12sp)
                .setTextGravity(Gravity.START)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setBackgroundColorResource(R.color.colorWhiteF9)
                .setLifecycleOwner(findViewTreeLifecycleOwner())
                .setIsVisibleArrow(true)
                .setArrowDrawableResource(R.drawable.tooltip_arrow)
                .setArrowSizeResource(R.dimen.dimen_14dp)
                .setArrowOrientation(ArrowOrientation.TOP)
                .setArrowPosition(ARROW_MIDDLE_POSITION)
                .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                .setDismissWhenClicked(true)
                .setOnBalloonClickListener {
                    onClick?.invoke()
                }

            ResourcesCompat.getFont(context, R.font.lato_bold)?.let(builder::setTextTypeface)
            image?.let(builder::setIconDrawableResource)
            builder.setTextResource(it)

            builder.build().showAlignBottom(view)
        }
    } ?: run {
        hide(true)
    }
}
