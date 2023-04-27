package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class IdentifierChannelDto : Parcelable {
    @SerializedName("sms")
    SMS,

    @SerializedName("email")
    EMAIL
}
