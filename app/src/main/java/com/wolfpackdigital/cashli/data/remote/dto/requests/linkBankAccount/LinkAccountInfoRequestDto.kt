package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountInfoRequestDto(
    val id: String? = null,
    val name: String? = null,
    val mask: String? = null,
    val subtype: LinkAccountSubtypeRequestDto? = null,
    @SerializedName("verification_status") val verificationStatus: LinkAccountVerificationStatusRequestDto? = null,
    val balance: LinkAccountBalanceRequestDto? = null
): Parcelable
