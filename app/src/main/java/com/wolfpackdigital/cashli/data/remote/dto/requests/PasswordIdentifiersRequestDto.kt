package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.RegistrationIdentifierChannelDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class PasswordIdentifiersRequestDto(
    @SerializedName("channel") val channel: RegistrationIdentifierChannelDto,
    @SerializedName("identifier") val identifier: String
) : Parcelable