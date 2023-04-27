package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountInfoRequestDto(
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("mask") val mask: String? = null,
    @SerializedName("subtype") val subtype: LinkAccountSubtypeRequestDto? = null,
    @SerializedName("verification_status") val verificationStatus: LinkAccountVerificationStatusRequestDto? = null,
    @SerializedName("balance") val balance: LinkAccountBalanceRequestDto? = null
) : Parcelable
