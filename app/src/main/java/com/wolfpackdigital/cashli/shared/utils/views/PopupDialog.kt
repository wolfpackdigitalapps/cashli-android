package com.wolfpackdigital.cashli.shared.utils.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wolfpackdigital.cashli.PopupDialogBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.drawableRes
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.visibility
import com.wolfpackdigital.cashli.shared.utils.extensions.startCounter
import java.util.concurrent.TimeUnit

@SuppressWarnings("LongParameterList")
class PopupDialog(
    private val context: Context,
    private val title: String,
    private val content: String,
    @DrawableRes private val topImage: Int,
    private val timerCount: Long? = null,
    private val buttonPrimary: String? = null,
    private val buttonSecondary: String? = null,
    private val buttonPrimaryClick: (AlertDialog) -> Unit = {},
    private val buttonSecondaryClick: (AlertDialog) -> Unit = {},
    private val buttonCloseClick: ((AlertDialog) -> Unit)? = null
) {
    private val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog
    private var countDownTimerObj: CountDownTimer? = null

    init {
        val binding = DataBindingUtil.inflate<PopupDialogBinding>(
            LayoutInflater.from(context), R.layout.popup_dialog_layout, null, false
        )
        builder.setView(binding.root)
        dialog = builder.create()

        dialog.setCancelable(false)
        setupViews(binding)
    }

    private fun setupViews(binding: PopupDialogBinding) {
        binding.tvTitle.text = title
        binding.ivTop.drawableRes(topImage)
        binding.tvContent.text = content
        binding.btnSecondary.apply {
            buttonSecondary?.let { buttonText ->
                text = buttonText
                setOnClickDebounced {
                    buttonSecondaryClick.invoke(dialog)
                    dialog.dismiss()
                }
            } ?: visibility(false)
        }
        binding.btnPrimary.apply {
            buttonPrimary?.let { buttonText ->
                text = buttonText
                setOnClickDebounced {
                    buttonPrimaryClick.invoke(dialog)
                    dialog.dismiss()
                }
            } ?: visibility(false)
        }
        binding.btnClose.apply {
            buttonCloseClick?.let {
                setOnClickDebounced {
                    buttonCloseClick.invoke(dialog)
                    dialog.dismiss()
                }
            } ?: visibility(false)
        }
        binding.clTimer.apply {
            timerCount?.let { countDownTime ->
                countDownTimerObj = startCounter(countDownTime, onTickCallback = { timer ->
                    updateTimerTextOnTick(timer, binding)
                }).start()
            } ?: visibility(false)
        }
        dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun updateTimerTextOnTick(timer: Long, binding: PopupDialogBinding) {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timer)
        if (seconds > 0) binding.tvTimer.text = seconds.toString()
        else {
            countDownTimerObj?.cancel()
            buttonCloseClick?.invoke(dialog)
            dialog.dismiss()
        }
    }

    fun show() {
        when (context) {
            is AppCompatActivity -> {
                if (!context.isFinishing) dialog.show()
            }
            else -> {
                dialog.dismiss()
            }
        }
    }

    fun dismiss() {
        dialog.dismiss()
    }
}
