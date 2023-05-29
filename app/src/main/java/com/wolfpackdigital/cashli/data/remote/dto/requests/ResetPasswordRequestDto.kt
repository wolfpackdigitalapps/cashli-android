package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguageDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResetPasswordRequestDto(
    @SerializedName("reset_password_token") val token: String,
    @SerializedName("password") val password: String,
    @SerializedName("password_confirmation") val passwordConfirmation: String,
    @SerializedName("locale") val locale: LanguageDto
) : Parcelable
