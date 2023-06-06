package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOutstandingBalanceStatusDto(
    @SerializedName("has_outstanding_balance") val outstandingBalance: Boolean,
    @SerializedName("cash_advance_balance_due") val cashAdvanceBalanceDue: String?,
    @SerializedName("repayment_date") val repaymentDate: String?
) : Parcelable
