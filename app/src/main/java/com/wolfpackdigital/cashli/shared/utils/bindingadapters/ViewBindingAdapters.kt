package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.text.Annotation
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_0
import com.wolfpackdigital.cashli.shared.utils.Constants.ALPHA_1
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS_300
import com.wolfpackdigital.cashli.shared.utils.CustomClickSpan
import com.wolfpackdigital.cashli.shared.utils.DebouncingOnClickListener
import com.wolfpackdigital.cashli.shared.utils.extensions.stringFromResource

private const val KEY_SPAN_ACTION = "action"
private const val FADE_ANIM_DURATION_200 = 200L

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
            is Int -> textArgs?.let { args -> context.stringFromResource(it, *args) }
                ?: context.stringFromResource(it)
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

@BindingAdapter(value = ["actions", "textWithActions"], requireAll = true)
fun TextView.setTextSpanByAction(actions: List<TextSpanAction>, @StringRes textWithActions: Int) {
    val termsText = context.getText(textWithActions) as? SpannedString ?: return
    val annotations = termsText.getSpans(0, termsText.length, Annotation::class.java)
    val termsCopy = SpannableString(termsText)
    annotations.forEach {
        if (it.key == KEY_SPAN_ACTION) {
            val action = actions.firstOrNull { action -> action.actionKey == it.value }
            val clickSpan = action?.let { textSpanAction ->
                CustomClickSpan(
                    onClickListener = textSpanAction.action,
                    textColor = context.getColor(textSpanAction.spanTextColor),
                    shouldUnderline = textSpanAction.isSpanTextUnderlined,
                    isBold = textSpanAction.isSpanTextBold
                )
            } ?: throw NotImplementedError(
                context.getString(R.string.generic_not_implemented_error)
            )
            termsCopy.setSpan(
                clickSpan,
                termsText.getSpanStart(it),
                termsText.getSpanEnd(it),
                SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    text = termsCopy
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = Color.TRANSPARENT
}

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
            animate().setDuration(FADE_ANIM_DURATION_200).alpha(ALPHA_1).setListener(endListener)        }
        View.INVISIBLE, View.GONE -> {
            animate().setDuration(FADE_ANIM_DURATION_200).alpha(ALPHA_0).setListener(endListener)
        }
    }
}
