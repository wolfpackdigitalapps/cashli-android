package com.wolfpackdigital.cashli.shared.base

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R> {

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: ApiError) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [Result] is of type [Success] & holds non-null [Success.data].
 */
val Result<*>.succeeded
    get() = this is Result.Success && data != null

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.data ?: fallback
}

inline fun <reified T> Result<T>.doIfError(callback: (exception: ApiError) -> Unit) {
    if (this is Result.Error) {
        callback(exception)
    }
}

inline fun <reified T> Result<T>.doIfSuccess(callback: (data: T) -> Unit) {
    if (this is Result.Success) {
        callback(data)
    }
}

@Parcelize
data class ApiError(
    val message: String? = null,
    val messageId: Int? = null,
    val code: String? = null,
    val errorCode: Int? = null,
    val errors: List<String>? = null
) : Parcelable
