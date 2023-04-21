package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentifiersTokenRequestDto(
    @SerializedName("email_token") val emailToken: String,
    @SerializedName("phone_number_token") val phoneNumberToken: String
) : Parcelable
