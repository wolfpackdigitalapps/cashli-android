package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton

@BindingAdapter("textRes")
fun MaterialButton.textRes(@StringRes textRes: Int?) {
    textRes ?: return
    setText(textRes)
}
