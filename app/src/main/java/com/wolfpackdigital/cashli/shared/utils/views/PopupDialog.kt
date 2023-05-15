package com.wolfpackdigital.cashli.shared.utils.views

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.wolfpackdigital.cashli.BR
import com.wolfpackdigital.cashli.PopupDialogBinding
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.shared.utils.bindingadapters.setOnClickDebounced
import com.wolfpackdigital.cashli.shared.utils.extensions.startCounter
import java.util.concurrent.TimeUnit

class PopupDialog(
    private val context: Context,
    private val popupConfig: PopupConfig
) {
    private val builder = AlertDialog.Builder(context)
    private var dialog: AlertDialog
    private var countDownTimerObj: CountDownTimer? = null

    init {
        val binding = DataBindingUtil.inflate<PopupDialogBinding>(
            LayoutInflater.from(context), R.layout.popup_dialog_layout, null, false
        ).also {
            it.setVariable(BR.viewModel, popupConfig)
        }

        builder.setView(binding.root)
        dialog = builder.create()

        dialog.setCancelable(false)
        setupViews(binding)
    }

    private fun setupViews(binding: PopupDialogBinding) {
        binding.btnSecondary.setOnClickDebounced {
            popupConfig.buttonSecondaryClick.invoke()
            dialog.dismiss()
        }
        binding.btnPrimary.setOnClickDebounced {
            popupConfig.buttonPrimaryClick.invoke()
            dialog.dismiss()
        }
        binding.btnClose.setOnClickDebounced {
            popupConfig.buttonCloseClick.invoke()
            countDownTimerObj?.cancel()
            dialog.dismiss()
        }
        binding.clTimer.apply {
            popupConfig.timerCount?.let { countDownTime ->
                countDownTimerObj = startCounter(
                    countDownTime,
                    onTickCallback = { timer ->
                        updateTimerTextOnTick(timer, binding)
                    }
                ).start()
            }
        }
        if (popupConfig.isOtherActionInstant)
            popupConfig.otherAction.invoke(dialog)
        dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
    }

    private fun updateTimerTextOnTick(timer: Long, binding: PopupDialogBinding) {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(timer)
        if (seconds > 0) {
            binding.tvTimer.text = seconds.toString()
        } else {
            countDownTimerObj?.cancel()
            popupConfig.buttonCloseClick.invoke()
            dialog.dismiss()
        }
    }

    fun show() {
        when {
            context is AppCompatActivity && !context.isFinishing -> dialog.show()
            else -> dialog.dismiss()
        }
    }
}
