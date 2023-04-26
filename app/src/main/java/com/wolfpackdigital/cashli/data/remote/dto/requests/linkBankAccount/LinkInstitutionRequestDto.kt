package com.wolfpackdigital.cashli.data.remote.dto.requests.linkBankAccount

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LinkInstitutionRequestDto(
    @SerializedName("institution_id") val id: String? = null,
    @SerializedName("institution_name") val name: String? = null
) : Parcelable
