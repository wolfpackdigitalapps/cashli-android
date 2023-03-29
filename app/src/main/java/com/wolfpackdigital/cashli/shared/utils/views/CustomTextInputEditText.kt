package com.wolfpackdigital.cashli.shared.utils.views

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.wolfpackdigital.cashli.R

private val STATE_ERROR = intArrayOf(R.attr.state_error)

class CustomTextInputEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {

    private var isError = false

    fun setError(error: Boolean) {
        isError = error
        refreshDrawableState()
    }

    override fun onCreateDrawableState(extraSpace: Int): IntArray {
        return if (isError) {
            val drawableState = super.onCreateDrawableState(extraSpace + 1)
            mergeDrawableStates(drawableState, STATE_ERROR)
            drawableState
        } else super.onCreateDrawableState(extraSpace)
    }
}
