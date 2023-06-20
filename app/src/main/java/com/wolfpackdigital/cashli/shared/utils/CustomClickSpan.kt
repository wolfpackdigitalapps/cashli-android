package com.wolfpackdigital.cashli.shared.utils

import android.os.SystemClock
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import androidx.annotation.ColorInt
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS_1000

class CustomClickSpan(
    private val onClickListener: () -> Unit,
    @ColorInt private val textColor: Int,
    private val shouldUnderline: Boolean = true
) : ClickableSpan() {
    private var lastTimeClicked: Long = 0
    override fun onClick(p0: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < DEBOUNCE_INTERVAL_MILLIS_1000) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onClickListener.invoke()
    }

    override fun updateDrawState(textPaint: TextPaint) {
        textPaint.isUnderlineText = shouldUnderline
        textPaint.color = textColor
    }
}
