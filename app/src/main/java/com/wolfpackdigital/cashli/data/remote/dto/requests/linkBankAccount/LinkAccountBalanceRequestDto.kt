package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountBalanceRequestDto(
    @SerializedName("available") val available: Double? = null,
    @SerializedName("currency") val currency: String? = null,
    @SerializedName("current") val current: Double? = null,
    @SerializedName("localized") val localized: LinkAccountLocalizedBalanceRequestDto? = null
) : Parcelable
