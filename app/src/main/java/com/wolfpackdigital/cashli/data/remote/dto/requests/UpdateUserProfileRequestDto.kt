package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguagesDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class UpdateUserProfileRequestDto(
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("street") val street: String,
    @SerializedName("zip_code") val zipCode: String,
    @SerializedName("city") val city: String,
    @SerializedName("state") val state: String,
    @SerializedName("language") val language: LanguagesDto = LanguagesDto.ENGLISH
) : Parcelable