package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TransferFeesDto(
    @SerializedName("id") val id: Int,
    @SerializedName("lower_limit_formatted") val lowerLimitFormatted: String,
    @SerializedName("lower_limit") val lowerLimit: Float,
    @SerializedName("upper_limit_formatted") val upperLimitFormatted: String,
    @SerializedName("upper_limit") val upperLimit: Float,
    @SerializedName("regular_fee") val regularFee: String,
    @SerializedName("next_day_fee") val nextDayFee: String,
    @SerializedName("same_day_fee") val sameDayFee: String,
    @SerializedName("instant_fee") val instantFee: String,
    @SerializedName("repayment_date") val repaymentDate: String?
) : Parcelable
