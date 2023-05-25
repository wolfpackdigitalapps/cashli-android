package com.wolfpackdigital.cashli.shared.notifications

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class NotificationModel(
    @SerializedName("type") val type: PushNotificationAction?,
    @SerializedName("title") val title: String?,
    @SerializedName("message") val message: String?
) : Parcelable

@Keep
@Parcelize
enum class PushNotificationAction : Parcelable {
    @SerializedName("low_balance")
    LOW_BALANCE,

    @SerializedName("user_eligible")
    USER_ELIGIBLE,

    @SerializedName("link_account_connection_lost")
    LINK_ACCOUNT_CONNECTION_LOST
}
