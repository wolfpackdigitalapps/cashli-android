package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.view.View
import androidx.databinding.BindingAdapter
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS
import com.wolfpackdigital.cashli.shared.utils.DebouncingOnClickListener

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter(value = ["android:onClick", "debounceInterval"], requireAll = false)
fun View.setOnClickBindingAdapter(
    onClickListener: View.OnClickListener,
    debounceInterval: Long?
) =
    setOnClickListener(
        DebouncingOnClickListener(
            debounceInterval ?: DEBOUNCE_INTERVAL_MILLIS,
            listener = onClickListener
        )
    )
