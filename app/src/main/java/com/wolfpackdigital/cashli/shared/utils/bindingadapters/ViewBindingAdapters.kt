package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS_300
import com.wolfpackdigital.cashli.shared.utils.DebouncingOnClickListener
import com.wolfpackdigital.cashli.shared.utils.extensions.stringFromResource

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("hidden")
fun View.hide(invisible: Boolean?) {
    this.visibility = if (invisible == true) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(value = ["android:onClick", "debounceInterval"], requireAll = false)
fun View.setOnClickDebounced(
    debounceInterval: Long? = DEBOUNCE_INTERVAL_MILLIS_300,
    onClickListener: View.OnClickListener
) = setOnClickListener(
    DebouncingOnClickListener(
        debounceInterval ?: DEBOUNCE_INTERVAL_MILLIS_300,
        listener = onClickListener
    )
)

@BindingAdapter("textRes")
fun TextView.textRes(@StringRes textRes: Int?) {
    textRes ?: return
    setText(textRes)
}
@BindingAdapter("textRes")
fun MaterialButton.textRes(@StringRes textRes: Int?) {
    textRes ?: return
    setText(textRes)
}

@Suppress("SpreadOperator")
@BindingAdapter(value = ["textIdOrString", "textArgs"], requireAll = false)
fun TextView.setTextIdOrString(textIdOrString: Any?, textArgs: Array<Any>?) {
    textIdOrString?.let {
        text = when (it) {
            is String -> it
            is Int -> textArgs?.let { args -> context.stringFromResource(it, *args) } ?: context.stringFromResource(it)
            else -> Constants.EMPTY_STRING
        }
    }
}
@BindingAdapter("drawableRes")
fun ImageView.drawableRes(@DrawableRes drawableRes: Int?) {
    drawableRes ?: return
    setImageResource(drawableRes)
}

@BindingAdapter("app:tint")
fun ImageView.setImageTint(@ColorInt color: Int) {
    setColorFilter(color)
}
