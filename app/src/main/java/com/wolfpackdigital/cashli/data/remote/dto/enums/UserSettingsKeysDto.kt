package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class UserSettingsKeysDto : Parcelable {
    @SerializedName("device_token")
    DEVICE_TOKEN,

    @SerializedName("low_balance_threshold")
    LOW_BALANCE_THRESHOLD,

    @SerializedName("plaid_item_id")
    PLAID_ITEM_ID,

    @SerializedName("push_notifications_enabled")
    PUSH_NOTIFICATIONS_ENABLED
}
