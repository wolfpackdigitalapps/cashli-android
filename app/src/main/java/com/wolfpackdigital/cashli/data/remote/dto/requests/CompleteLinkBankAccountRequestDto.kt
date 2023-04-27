package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount.LinkAccountMetadataRequestDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class CompleteLinkBankAccountRequestDto(
    @SerializedName("public_token") val publicToken: String,
    @SerializedName("metadata") val metadata: LinkAccountMetadataRequestDto
) : Parcelable
