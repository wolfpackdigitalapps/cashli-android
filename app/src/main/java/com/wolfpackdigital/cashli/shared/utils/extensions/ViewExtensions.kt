package com.wolfpackdigital.cashli.shared.utils.extensions

import android.graphics.Color
import android.text.Annotation
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.shared.utils.CustomClickSpan

private const val KEY_SPAN_ACTION = "action"

// Extensions related to views (Views, TextViews, etc...)

@BindingAdapter(
    value = ["actions", "textWithActions"],
    requireAll = true
)
fun TextView.setTextSpanByAction(
    actions: List<TextSpanAction>,
    @StringRes textWithActions: Int
) {
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
