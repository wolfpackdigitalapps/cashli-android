package com.wolfpackdigital.cashli.shared.utils.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.Constants.PHONE_NUMBER_PREFIX_LABEL
import com.wolfpackdigital.cashli.shared.utils.Constants.SUPPORT_PHONE_NUMBER
import com.wolfpackdigital.cashli.shared.utils.views.PopupDialog

fun Fragment.snackBar(message: String, action: ((View) -> Unit)? = {}, actionText: String? = null) {
    this.view?.let {
        Snackbar.make(
            it, message, Snackbar.LENGTH_LONG
        ).setAction(actionText, action).show()
    }
}

fun Activity.snackBar(message: String, action: ((View) -> Unit)? = {}, actionText: String? = null) {
    this.window?.decorView?.let {
        Snackbar.make(
            it, message, Snackbar.LENGTH_LONG
        ).setAction(actionText, action).show()
    }
}

@Suppress("ComplexMethod", "SpreadOperator")
fun Fragment.showPopupById(command: BaseCommand.ShowPopupById): PopupDialog? {
    context?.let { ctx ->
        val popupDialog = PopupDialog(
            ctx,
            ctx.stringFromResource(command.titleId),
            command.contentId?.let { contentId ->
                command.contentFormatArgs?.let {
                    ctx.stringFromResource(contentId, *it)
                } ?: ctx.stringFromResource(contentId)
            } ?: command.content ?: EMPTY_STRING,
            command.imageId,
            command.timerCount,
            command.buttonPrimaryId?.let { ctx.stringFromResource(it) },
            command.buttonSecondaryId?.let { ctx.stringFromResource(it) },
            command.buttonPrimaryClick,
            command.buttonSecondaryClick,
            command.buttonCloseClick
        )
        popupDialog.show()
        return popupDialog
    }
    return null
}

@SuppressWarnings("LongParameterList")
fun Activity.showDialog(
    title: String = "",
    message: String = "",
    isCancelable: Boolean = true,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    positiveButtonClick: () -> Unit = {},
    negativeButtonClick: () -> Unit = {}
): AlertDialog {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title).setMessage(message).setCancelable(isCancelable)
    positiveButtonText?.let { text ->
        builder.setPositiveButton(text) { dialog, _ ->
            dialog.dismiss()
            positiveButtonClick()
        }
    }
    negativeButtonText?.let { text ->
        builder.setNegativeButton(text) { dialog, _ ->
            dialog.dismiss()
            negativeButtonClick()
        }
    }
    return builder.show()
}

inline fun <reified T : Parcelable> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.parcelable<T>(key)
    if (value is T) value else default
}

inline fun <reified T : Parcelable> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.parcelable<T>(key)
    requireNotNull(if (value is T) value else default) { key }
}

val Fragment.navController: NavController?
    get() = runCatching { findNavController() }.getOrNull()

val Activity.navController: NavController?
    get() = runCatching { findNavController(R.id.main_host_fragment) }.getOrNull()

fun Activity.showDialer(phoneNumber: String = SUPPORT_PHONE_NUMBER) {
    Intent(Intent.ACTION_DIAL).apply {
        data = Uri.parse("$PHONE_NUMBER_PREFIX_LABEL$phoneNumber")
        startActivity(this)
    }
}
