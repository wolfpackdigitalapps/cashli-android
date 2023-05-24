package com.wolfpackdigital.cashli.shared.utils.inputFilters

import android.text.InputFilter
import android.text.Spanned
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

private const val ZERO_STRING = "0"

@Suppress("ReturnCount")
class WholeNumberFilter : InputFilter {
    override fun filter(
        source: CharSequence?,
        start: Int,
        end: Int,
        dest: Spanned?,
        dStart: Int,
        dEnd: Int
    ): CharSequence {
        source ?: return EMPTY_STRING
        if (dest.toString() == ZERO_STRING && source == ZERO_STRING)
            return EMPTY_STRING
        return source
    }
}
