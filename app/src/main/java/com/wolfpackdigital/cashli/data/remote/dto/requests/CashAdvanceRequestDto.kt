package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.DeliveryMethodDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashAdvanceRequestDto(
    @SerializedName("amount") val amount: Float,
    @SerializedName("tip") val tip: Float,
    @SerializedName("transfer_channel") val transferChannel: DeliveryMethodDto
) : Parcelable