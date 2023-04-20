package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class RegistrationIdentifierChannelDto : Parcelable {
    @SerializedName("sms")
    SMS,

    @SerializedName("email")
    EMAIL
}
