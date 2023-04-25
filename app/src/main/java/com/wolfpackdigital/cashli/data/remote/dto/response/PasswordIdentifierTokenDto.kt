package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordIdentifierTokenDto(
    @SerializedName("reset_password_token") val token: String
) : Parcelable