package com.wolfpackdigital.cashli.data.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.UserSettingsKeysDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserSettingDto(
    @SerializedName("key") val key: UserSettingsKeysDto,
    @SerializedName("value") val value: String
) : Parcelable
