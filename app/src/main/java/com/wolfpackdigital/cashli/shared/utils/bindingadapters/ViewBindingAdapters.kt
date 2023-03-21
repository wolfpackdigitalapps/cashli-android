package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}
