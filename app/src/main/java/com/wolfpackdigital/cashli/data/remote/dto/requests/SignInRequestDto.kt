package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInRequestDto(
    @SerializedName("user") val userSignInRequest: UserSignInRequestDto,
) : Parcelable
