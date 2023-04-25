package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordIdentifiersCodeValidationRequestDto(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("code") val code: String
) : Parcelable