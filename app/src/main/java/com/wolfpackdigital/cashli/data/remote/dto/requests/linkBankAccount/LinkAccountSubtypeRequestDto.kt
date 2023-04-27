package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountSubtypeRequestDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: LinkAccountTypeRequestDto? = null
) : Parcelable
