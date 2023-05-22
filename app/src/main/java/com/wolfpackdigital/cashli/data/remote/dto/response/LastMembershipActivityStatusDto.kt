package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class LastMembershipActivityStatusDto(val isActive: Boolean) : Parcelable {
    @SerializedName("active")
    ACTIVE(true),

    @SerializedName("paused")
    PAUSED(false)
}