@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.shared.utils.extensions

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.view.View
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
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

@SuppressWarnings("LongParameterList")
fun Fragment.showDialog(
    title: String = EMPTY_STRING,
    message: String = EMPTY_STRING,
    isCancelable: Boolean = true,
    positiveButtonText: String? = null,
    negativeButtonText: String? = null,
    positiveButtonClick: () -> Unit = {},
    negativeButtonClick: () -> Unit = {}
): AlertDialog? {
    this.context?.let {
        val builder = AlertDialog.Builder(it)
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
    return null
}

@Suppress("ComplexMethod", "SpreadOperator")
fun Fragment.showPopupById(popupConfig: PopupConfig): PopupDialog? {
    context?.let { ctx ->
        val popupDialog = PopupDialog(ctx, popupConfig)
        popupDialog.show()
        return popupDialog
    }
    return null
}

@SuppressWarnings("LongParameterList")
fun Activity.showDialog(
    title: String = EMPTY_STRING,
    message: String = EMPTY_STRING,
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

inline fun <reified T : Parcelable> Fragment.extraNotNull(key: String, default: T? = null) =
    lazy {
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

fun Context.openUrl(urlResource: Int) {
    val builder = CustomTabsIntent.Builder().apply {
        val darkParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(this@openUrl, R.color.colorPrimary))
            .build()
        setDefaultColorSchemeParams(darkParams)
    }
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this@openUrl, Uri.parse(getString(urlResource)))
}

fun <T> NavController.setBackStackData(key: String, data: T?) =
    previousBackStackEntry?.savedStateHandle?.set(key, data)

fun <T> NavController.getBackStackData(
    key: String,
    owner: LifecycleOwner,
    removeValue: Boolean = false,
    removeObserver: Boolean = false,
    result: (T?) -> (Unit)
) {
    val savedState = currentBackStackEntry?.savedStateHandle
    savedState?.getLiveData<T?>(key)
        ?.observe(owner) {
            if (!removeObserver && removeValue && it != null) savedState.set(key, null)
            if (removeObserver) savedState.remove<T>(key)
            result(it)
        }
}
