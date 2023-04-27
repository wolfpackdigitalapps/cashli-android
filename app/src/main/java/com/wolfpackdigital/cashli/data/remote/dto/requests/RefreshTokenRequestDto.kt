package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RefreshTokenRequestDto(
    @SerializedName("grant_type")
    val grantType: String,
    @SerializedName("refresh_token")
    val refreshToken: String
) : Parcelable
