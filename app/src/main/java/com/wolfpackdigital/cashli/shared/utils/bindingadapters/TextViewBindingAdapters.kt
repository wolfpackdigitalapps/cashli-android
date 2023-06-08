package com.wolfpackdigital.cashli.shared.utils.bindingadapters

import android.graphics.Color
import android.text.Annotation
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.databinding.BindingAdapter
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.BankAccountSubtype
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.CustomClickSpan
import com.wolfpackdigital.cashli.shared.utils.extensions.daysBetweenDates
import com.wolfpackdigital.cashli.shared.utils.extensions.getStringFromResourceOrText
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDate
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedZonedDate
import com.wolfpackdigital.cashli.shared.utils.extensions.toLocalDateFromPatternOrNull
import java.time.LocalDate

private const val KEY_SPAN_ACTION = "action"
private const val TAG = "TextViewBindingAdapters"

@BindingAdapter("textRes")
fun TextView.textRes(@StringRes textRes: Int?) {
    textRes ?: return
    try {
        setText(textRes)
    } catch (e: Exception) {
        Log.e(TAG, "textRes: ${e.stackTrace}")
    }
}

@Suppress("SpreadOperator")
@BindingAdapter(value = ["textIdOrString", "textArgs"], requireAll = false)
fun TextView.setTextIdOrString(textIdOrString: Any?, textArgs: Array<Any>?) {
    textIdOrString?.let {
        text = getStringFromResourceOrText(context, it, textArgs)
    }
}

@BindingAdapter(value = ["actions", "textWithActions"], requireAll = true)
fun TextView.setTextSpanByAction(actions: List<TextSpanAction>?, @StringRes textWithActions: Int) {
    val termsText = context.getText(textWithActions) as? SpannedString
    termsText?.let {
        val termsCopy = SpannableString(termsText)
        actions?.let {
            val annotations = termsText.getSpans(0, termsText.length, Annotation::class.java)
            annotations.forEach {
                setClickSpanForAction(it, actions, termsCopy, termsText)
            }
        }
        text = termsCopy
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    } ?: textRes(textWithActions)
}

private fun TextView.setClickSpanForAction(
    it: Annotation,
    actions: List<TextSpanAction>,
    termsCopy: SpannableString,
    termsText: SpannedString
) {
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

@BindingAdapter(
    value = ["requirement_text", "tooltip_text", "requirement_image"],
    requireAll = true
)
fun TextView.setCustomTextAndBalloon(
    text: String,
    tooltipText: String,
    @DrawableRes image: Int
) {
    val string = SpannableString(text + Constants.DOUBLE_SPACE)
    string.setSpan(
        ImageSpan(context, image), string.length - 1, string.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    val clickableSpan: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            Balloon.Builder(context)
                .setIsVisibleArrow(false)
                .setWidth(BalloonSizeSpec.WRAP)
                .setHeight(BalloonSizeSpec.WRAP)
                .setText(tooltipText)
                .setPaddingResource(R.dimen.dimen_16dp)
                .setPaddingRightResource(R.dimen.dimen_32dp)
                .setCornerRadiusResource(R.dimen.dimen_8dp)
                .setTextColorResource(R.color.colorGrayB6)
                .setTextSizeResource(R.dimen.size_12sp)
                .setTextGravity(Gravity.START)
                .setBalloonAnimation(BalloonAnimation.FADE)
                .setBackgroundColorResource(R.color.colorWhiteF9)
                .setLifecycleOwner(findViewTreeLifecycleOwner())
                .build()
                .showAlignBottom(this@setCustomTextAndBalloon)
        }
    }
    string.setSpan(
        clickableSpan,
        string.length - 1,
        string.length,
        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    movementMethod = LinkMovementMethod.getInstance()
    setText(string)
}

@BindingAdapter("bankSubtypeAndMask", "stringRes")
fun TextView.setBankAccountSubtypeAndMask(bankAccount: BankAccount?, @StringRes stringRes: Int?) {
    val stringResource = stringRes ?: R.string.bank_account_subtype_and_mask
    bankAccount ?: return
    val bankSubtype = when (bankAccount.accountSubtype) {
        BankAccountSubtype.CHECKING -> context.getString(R.string.bank_account_subtype_checking)
        BankAccountSubtype.DEPOSITORY -> context.getString(R.string.bank_account_subtype_depository)
        BankAccountSubtype.SAVINGS -> context.getString(R.string.bank_account_subtype_savings)
    }
    text = context.getString(
        stringResource,
        bankSubtype,
        bankAccount.accountNumberMask
    )
}

@BindingAdapter(value = ["isZonedDate", "stringDate", "stringRes"], requireAll = false)
fun TextView.setTextWithDate(
    isZonedDate: Boolean?,
    stringDate: String?,
    @StringRes stringRes: Int?
) {
    stringDate ?: return

    val date = when (isZonedDate) {
        true -> stringDate.toFormattedZonedDate()
        false -> stringDate.toFormattedLocalDate()
        else -> null
    } ?: Constants.DASH

    text = stringRes?.let { context.getString(stringRes, date) } ?: date
}

@BindingAdapter(
    value = ["pastDaysText", "dateFormat"],
    requireAll = false
)
fun TextView.setPastDaysText(dateString: String?, dateFormat: String?) {
    dateString ?: return
    val oldDate = dateString.toLocalDateFromPatternOrNull(dateFormat) ?: return
    val today = LocalDate.now()
    val days = daysBetweenDates(oldDate, today)
    text = resources.getQuantityString(
        R.plurals.past_days_plural,
        days,
        days
    )
}
