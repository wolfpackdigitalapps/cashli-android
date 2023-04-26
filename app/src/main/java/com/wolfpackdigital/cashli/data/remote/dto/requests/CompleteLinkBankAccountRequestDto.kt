package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class CompleteLinkBankAccountRequestDto(
    @SerializedName("public_token") val publicToken: String,
    @SerializedName("metadata") val metadata: @RawValue Any
) : Parcelable
