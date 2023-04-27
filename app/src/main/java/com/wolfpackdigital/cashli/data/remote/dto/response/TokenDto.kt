package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokenDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("token_type")
    val tokenType: String,
    @SerializedName("expires_in")
    val expiresIn: Double,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("created_at")
    val createdAt: Double
) : Parcelable
