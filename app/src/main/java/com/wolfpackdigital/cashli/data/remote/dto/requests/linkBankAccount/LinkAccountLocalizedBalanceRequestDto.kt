package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountLocalizedBalanceRequestDto(
    @SerializedName("available") val available: String? = null,
    @SerializedName("current") val current: String? = null,
) : Parcelable
