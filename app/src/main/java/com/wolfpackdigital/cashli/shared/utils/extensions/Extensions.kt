@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.shared.utils.extensions

import android.content.Intent
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.shared.base.ApiError
import com.wolfpackdigital.cashli.shared.utils.Constants.DEBOUNCE_INTERVAL_MILLIS_1000
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import retrofit2.HttpException
import java.util.regex.Pattern

private const val GENERIC_SERVER_ERROR = "Something went wrong, please try again."
private const val PARSING_SERVER_ERROR = "The response could not be parsed"
private const val SYNTAX_SERVER_ERROR = "The response doesn't have a valid format"

fun Throwable.getParsedError(): String = try {
    val response = (this as? HttpException)?.response()?.errorBody()
    val model = Gson().fromJson(response?.string(), ApiError::class.java)
    model?.errors?.firstOrNull() ?: model?.message ?: GENERIC_SERVER_ERROR
} catch (ex: JsonParseException) {
    ex.localizedMessage ?: PARSING_SERVER_ERROR
} catch (ex: JsonSyntaxException) {
    ex.localizedMessage ?: SYNTAX_SERVER_ERROR
}

val appVersion: String
    get() = "v${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})"

fun String.hasEmailPattern(): Boolean {
    val pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+"
    )
    return pattern.matcher(this).matches()
}

operator fun MutableLiveData<Int>.minusAssign(t: Int) {
    val current = this.value ?: 0
    this.value = current - t
}

operator fun MutableLiveData<Int>.plusAssign(t: Int) {
    val current = this.value ?: 0
    this.value = current + t
}

fun <T : Any, R : Any> whenAllNotNull(vararg options: T?, block: (List<T>) -> R) {
    if (options.all { it != null }) {
        block(options.filterNotNull()) // or do unsafe cast to non null collection
    }
}

fun <T : Any, R : Any> whenAnyNotNull(vararg options: T?, block: (List<T>) -> R) {
    if (options.any { it != null }) {
        block(options.filterNotNull())
    }
}

inline fun <T : Any> guardLet(vararg elements: T?, closure: () -> Nothing): List<T> {
    return if (elements.all { it != null }) {
        elements.filterNotNull()
    } else {
        closure()
    }
}

inline fun guard(vararg elements: Boolean, closure: () -> Nothing) {
    if (!elements.all { it }) {
        closure()
    }
}

fun <T1 : Any, T2 : Any, R : Any> safeLet(p1: T1?, p2: T2?, block: (T1, T2) -> R?): R? {
    return if (p1 != null && p2 != null) block(p1, p2) else null
}

// Usage: condition then "yes" ?: "no"
infix fun <T> Boolean.then(param: T): T? = if (this) param else null

inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelableExtra(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T
}

fun initTimer(totalSeconds: Int): Flow<Int> =
    (totalSeconds - 1 downTo 0).asFlow()
        .onEach { delay(DEBOUNCE_INTERVAL_MILLIS_1000) }
        .onStart { emit(totalSeconds) }
        .conflate() // In case the operation onTick takes some time, conflate keeps the time ticking separately
        .transform { emit(it) }
