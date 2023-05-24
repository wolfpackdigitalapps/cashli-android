package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DeliveryMethodDto : Parcelable {
    @SerializedName("standard")
    STANDARD,
    @SerializedName("same_day")
    SAME_DAY,
    @SerializedName("next_day")
    NEXT_DAY,
    @SerializedName("express")
    EXPRESS
}
