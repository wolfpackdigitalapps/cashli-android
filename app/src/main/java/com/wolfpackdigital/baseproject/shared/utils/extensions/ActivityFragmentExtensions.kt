package com.wolfpackdigital.baseproject.shared.utils.extensions

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wolfpackdigital.baseproject.R

fun Fragment.snackBar(message: String, action: ((View) -> Unit)? = {}, actionText: String? = null) {
    this.view?.let {
        Snackbar.make(
            it,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(actionText, action).show()
    }
}

fun Activity.snackBar(message: String, action: ((View) -> Unit)? = {}, actionText: String? = null) {
    this.window?.decorView?.let {
        Snackbar.make(
            it,
            message,
            Snackbar.LENGTH_LONG
        ).setAction(actionText, action).show()
    }
}

@SuppressWarnings("LongParameterList")
fun Fragment.showDialog(
    title: String = "",
    message: String = "",
    isCancelable: Boolean = true,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    positiveButtonClick: () -> Unit = {},
    negativeButtonClick: () -> Unit = {}
): AlertDialog? {
    this.context?.let {
        val builder = AlertDialog.Builder(it)
        builder.setTitle(title)
            .setMessage(message)
            .setCancelable(isCancelable)
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
    builder.setTitle(title)
        .setMessage(message)
        .setCancelable(isCancelable)
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

inline fun <reified T : Any> Fragment.extra(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    if (value is T) value else default
}

inline fun <reified T : Any> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.get(key)
    requireNotNull(if (value is T) value else default) { key }
}

val Fragment.navController: NavController?
    get() = runCatching { findNavController() }.getOrNull()

val Activity.navController: NavController?
    get() = runCatching { findNavController(R.id.main_host_fragment) }.getOrNull()
