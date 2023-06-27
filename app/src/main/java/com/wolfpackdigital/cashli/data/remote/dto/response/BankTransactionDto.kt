package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class BankTransactionDto(
    @SerializedName("transaction_id") val transactionId: Int,
    @SerializedName("date") val date: String?,
    @SerializedName("name") val merchantName: String?,
    @SerializedName("amount") val amount: String?
) : Parcelable
