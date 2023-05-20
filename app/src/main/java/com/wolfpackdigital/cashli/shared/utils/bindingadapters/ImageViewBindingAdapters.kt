package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.view.Gravity
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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

private const val ARROW_MIDDLE_POSITION = 0.5f

@BindingAdapter("drawableRes")
fun ImageView.drawableRes(@DrawableRes drawableRes: Int?) {
    drawableRes ?: return
    setImageResource(drawableRes)
}

@BindingAdapter("app:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}

@BindingAdapter(value = ["tooltip_text", "requirement_image", "onClick"])
fun ImageView.setCustomTextBalloon(
    @StringRes tooltipText: Int?,
    @DrawableRes image: Int?,
    onClick: (() -> Unit)?
) {
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
            .setMarginRightResource(R.dimen.dimen_30dp)
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
        tooltipText?.let(builder::setTextResource)

        builder.build().showAlignBottom(view)
    }
}
