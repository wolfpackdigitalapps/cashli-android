package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSettingDto(
    @SerializedName("key") val key: String,
    @SerializedName("value") val value: String
) : Parcelable
