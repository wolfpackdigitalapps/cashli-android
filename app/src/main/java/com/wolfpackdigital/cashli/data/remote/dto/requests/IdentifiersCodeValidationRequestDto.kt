package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguageDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentifiersCodeValidationRequestDto(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("code") val code: String,
    @SerializedName("locale") val locale: LanguageDto
) : Parcelable
