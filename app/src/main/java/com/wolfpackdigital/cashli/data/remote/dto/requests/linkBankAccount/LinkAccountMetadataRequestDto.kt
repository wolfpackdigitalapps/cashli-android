package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkAccountMetadataRequestDto(
    val accounts: List<LinkAccountInfoRequestDto>? = null,
    val institution: LinkInstitutionRequestDto? = null,
    @SerializedName("link_session_id") val linkSessionId: String? = null,
    @SerializedName("metadata_json") val metadataJson: String? = null
) : Parcelable
