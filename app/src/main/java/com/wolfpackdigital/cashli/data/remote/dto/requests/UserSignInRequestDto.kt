package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
class UserSignInRequestDto(
    @SerializedName("identifier") val identifier: String,
    @SerializedName("password") val password: String
) : Parcelable