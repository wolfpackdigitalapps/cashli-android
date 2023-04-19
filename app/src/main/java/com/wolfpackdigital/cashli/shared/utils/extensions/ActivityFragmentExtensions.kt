@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.shared.utils.extensions

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.Toast
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
import com.wolfpackdigital.cashli.shared.utils.views.PopupDialog
import java.security.InvalidParameterException

// Support
private const val SUPPORT_PHONE_NUMBER = "+1773-234-7405"
private const val PHONE_NUMBER_PREFIX_LABEL = "tel:"
private const val SMS_PHONE_NUMBER_PREFIX_LABEL = "smsto:"

// SnackBar
private const val SNAKEBAR_TAG = "SNACKBAR_ANCHOR"
private const val SNAKEBAR_MSG = "snackBar: FRAGMENT"

fun <T> Fragment.showMessage(message: T, isToast: Boolean = true) {
    showMessageByType(isToast, message)
}

fun <T> Fragment.showMessageByType(isToast: Boolean, message: T) {
    when {
        isToast -> toast(message)
        else -> snackBar(message)
    }
}

fun <T> Activity.showMessage(message: T, isToast: Boolean = true) {
    showMessageByType(isToast, message)
}

fun <T> Activity.showMessageByType(isToast: Boolean, message: T) {
    when {
        isToast -> toast(message)
        else -> snackBar(message)
    }
}

fun <T> Fragment.toast(message: T) {
    when (message) {
        is String -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        is Int -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        else -> throw InvalidParameterException()
    }
}

fun <T> Fragment.snackBar(
    message: T,
    action: ((View) -> Unit)? = {},
    actionText: String? = null,
    bottomMargin: Int? = null
) {
    this.view?.let {
        when (message) {
            is String -> createSnackbar(it, message, bottomMargin, actionText, action)
            is Int -> createSnackbarById(it, message, bottomMargin, actionText, action)
            else -> throw InvalidParameterException()
        }
    }
}

fun <T> Activity.snackBar(
    message: T,
    action: ((View) -> Unit)? = {},
    actionText: String? = null,
    bottomMargin: Int? = null
) {
    this.window?.decorView?.let {
        when (message) {
            is String -> createSnackbar(it, message, bottomMargin, actionText, action)
            is Int -> createSnackbarById(it, message, bottomMargin, actionText, action)
            else -> throw InvalidParameterException()
        }
    }
}

fun <T> Activity.toast(message: T) {
    when (message) {
        is String -> Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        is Int -> Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        else -> throw InvalidParameterException()
    }
}

@SuppressLint("ShowToast")
private fun createSnackbar(
    it: View,
    message: String,
    bottomMargin: Int?,
    actionText: String?,
    action: ((View) -> Unit)?
) {
    Snackbar.make(it, message, Snackbar.LENGTH_LONG).apply {
        showSnackbar(bottomMargin, actionText, action)
    }
}

@SuppressLint("ShowToast")
private fun createSnackbarById(
    it: View,
    resId: Int,
    bottomMargin: Int?,
    actionText: String?,
    action: ((View) -> Unit)?
) {
    Snackbar.make(it, resId, Snackbar.LENGTH_LONG).apply {
        showSnackbar(bottomMargin, actionText, action)
    }
}

private fun Snackbar.showSnackbar(
    bottomMargin: Int?,
    actionText: String?,
    action: ((View) -> Unit)?
) {
    try {
        setAnchorView(R.id.main_bottom_nav)
    } catch (iae: IllegalArgumentException) {
        Log.e(SNAKEBAR_TAG, SNAKEBAR_MSG, iae)
    }
    bottomMargin?.let { margin ->
        this.view.translationY = (-margin).toFloat()
    }
    setAction(actionText, action)
    show()
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

inline fun <reified T : Parcelable> Fragment.extraNotNull(key: String, default: T? = null) = lazy {
    val value = arguments?.parcelable<T>(key)
    requireNotNull(if (value is T) value else default) { key }
}

val Fragment.navController: NavController?
    get() = runCatching { findNavController() }.getOrNull()

val Activity.navController: NavController?
    get() = runCatching { findNavController(R.id.main_host_fragment) }.getOrNull()

fun Activity.showDialer(phoneNumber: String = SUPPORT_PHONE_NUMBER) {
    try {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("$PHONE_NUMBER_PREFIX_LABEL$phoneNumber")
            startActivity(this)
        }
    } catch (exception: ActivityNotFoundException) {
        snackBar(getString(R.string.no_messaging_app_found))
    }
}

fun Activity.showSMSApp(phoneNumber: String = SUPPORT_PHONE_NUMBER) {
    try {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("$SMS_PHONE_NUMBER_PREFIX_LABEL$phoneNumber")
            startActivity(this)
        }
    } catch (exception: ActivityNotFoundException) {
        snackBar(getString(R.string.no_messaging_app_found))
    }
}

fun Context.openUrl(urlResource: Int) {
    val builder = CustomTabsIntent.Builder().apply {
        val darkParams = CustomTabColorSchemeParams.Builder()
            .setToolbarColor(ContextCompat.getColor(this@openUrl, R.color.colorPrimary)).build()
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
    savedState?.getLiveData<T?>(key)?.observe(owner) {
        if (!removeObserver && removeValue && it != null) savedState.set(key, null)
        if (removeObserver) savedState.remove<T>(key)
        result(it)
    }
}
