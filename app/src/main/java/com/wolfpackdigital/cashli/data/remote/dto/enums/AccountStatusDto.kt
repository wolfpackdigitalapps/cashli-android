package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class AccountStatusDto : Parcelable {
    @SerializedName("active")
    ACTIVE,

    @SerializedName("locked")
    LOCKED,

    @SerializedName("paused")
    PAUSED,

    @SerializedName("closed")
    CLOSED,

    @SerializedName("suspended")
    SUSPENDED
}