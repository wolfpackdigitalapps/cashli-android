package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankTokenDto(
    @SerializedName("link_token")
    val linkToken: String,
    @SerializedName("expiration")
    val expiration: String,
    @SerializedName("redirect_uri")
    val redirectUri: String?
) : Parcelable
