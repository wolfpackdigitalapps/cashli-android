package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.databinding.BindingAdapter

@BindingAdapter("drawableRes")
fun ImageView.drawableRes(@DrawableRes drawableRes: Int?) {
    drawableRes ?: return
    setImageResource(drawableRes)
}

@BindingAdapter("app:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}
