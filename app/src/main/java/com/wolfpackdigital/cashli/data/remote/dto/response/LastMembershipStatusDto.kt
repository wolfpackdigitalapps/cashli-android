package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LastMembershipStatusDto : Parcelable {
    @SerializedName("payment_pending")
    PAYMENT_PENDING,
    @SerializedName("active")
    ACTIVE,
    @SerializedName("expired")
    EXPIRED
}