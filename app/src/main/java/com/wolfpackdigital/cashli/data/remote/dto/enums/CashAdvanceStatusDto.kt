package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class CashAdvanceStatusDto : Parcelable {
    @SerializedName("overdue")
    OVERDUE,

    @SerializedName("paid")
    PAID,

    @SerializedName("scheduled")
    SCHEDULED
}
