package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.IdentifierChannelDto
import com.wolfpackdigital.cashli.data.remote.dto.enums.LanguageDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class IdentifiersRequestDto(
    @SerializedName("channel") val channel: IdentifierChannelDto,
    @SerializedName("identifier") val identifier: String,
    @SerializedName("locale") val locale: LanguageDto
) : Parcelable
