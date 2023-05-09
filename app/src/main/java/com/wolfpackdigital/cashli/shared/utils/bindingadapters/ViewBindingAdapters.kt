@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_0
import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_1
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS_300
import com.wolfpackdigital.cashli.shared.utils.DebouncingOnClickListener

private const val FADE_ANIM_DURATION_200 = 200L

@BindingConversion
fun convertBooleanToVisibility(isVisible: Boolean?): Int {
    return if (isVisible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("visibility")
fun View.visibility(visible: Boolean?) {
    this.visibility = if (visible == true) View.VISIBLE else View.GONE
}

@BindingAdapter("hidden")
fun View.hide(invisible: Boolean?) {
    this.visibility = if (invisible == true) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter(value = ["debounceInterval", "android:onClick"], requireAll = false)
fun View.setOnClickDebounced(
    debounceInterval: Long? = DEBOUNCE_INTERVAL_MILLIS_300,
    onClickListener: View.OnClickListener
) = setOnClickListener(
    DebouncingOnClickListener(
        debounceInterval ?: DEBOUNCE_INTERVAL_MILLIS_300,
        listener = onClickListener
    )
)

@BindingAdapter("viewState")
fun View.setFadeVisibility(viewState: Int?) {
    viewState ?: return
    val endListener = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            alpha = ALPHA_1
            visibility = viewState
        }
    }
    when (viewState) {
        View.VISIBLE -> {
            visibility = View.VISIBLE
            alpha = ALPHA_0
            animate().setDuration(FADE_ANIM_DURATION_200).alpha(ALPHA_1).setListener(endListener)
        }

        View.INVISIBLE, View.GONE -> {
            animate().setDuration(FADE_ANIM_DURATION_200).alpha(ALPHA_0).setListener(endListener)
        }
    }
}
