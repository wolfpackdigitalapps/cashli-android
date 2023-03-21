package com.wolfpackdigital.cashli.shared.notifications

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NotificationModel(
    val title: String?,
    val body: String?,
    val action: PushNotificationAction?
) : Parcelable

@Keep
enum class PushNotificationAction
