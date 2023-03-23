package com.wolfpackdigital.cashli.shared.utils

import android.view.View
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS
import com.wolfpackdigital.cashli.shared.utils.DebouncingClickEnabled.ENABLE_AGAIN
import com.wolfpackdigital.cashli.shared.utils.DebouncingClickEnabled.enabled

class DebouncingOnClickListener(
    private val debounceInterval: Long = DEBOUNCE_INTERVAL_MILLIS,
    private val listener: View.OnClickListener
) : View.OnClickListener {
    override fun onClick(v: View?) {
        if (enabled) {
            v?.run {
                enabled = false
                v.postDelayed(ENABLE_AGAIN, debounceInterval)
                listener.onClick(v)
            }
        }
    }
}

object DebouncingClickEnabled {
    @JvmStatic
    var enabled = true
    val ENABLE_AGAIN = Runnable { enabled = true }
}
