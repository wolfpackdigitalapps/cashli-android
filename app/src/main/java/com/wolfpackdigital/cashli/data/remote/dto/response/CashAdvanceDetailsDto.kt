package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.CashAdvanceStatusDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class CashAdvanceDetailsDto(
    @SerializedName("amount") val amount: String,
    @SerializedName("tip") val tip: String,
    @SerializedName("payout_fee") val payoutFee: String,
    @SerializedName("total_repayable") val totalRepayable: String,
    @SerializedName("cash_advance_status") val cashAdvanceStatus: CashAdvanceStatusDto,
    @SerializedName("due_date") val dueDate: String,
    @SerializedName("paid_date") val paidDate: String
) : Parcelable
