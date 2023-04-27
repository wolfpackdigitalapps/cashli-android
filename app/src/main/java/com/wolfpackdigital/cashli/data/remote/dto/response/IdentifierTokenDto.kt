package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentifierTokenDto(
    @SerializedName("token") val token: String
) : Parcelable
