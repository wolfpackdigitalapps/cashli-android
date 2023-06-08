package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.wolfpackdigital.cashli.data.remote.dto.enums.EligibilityStatusDto
import kotlinx.parcelize.Parcelize

@Parcelize
data class EligibilityChecksDto(
    @SerializedName("max_cash_advance") val maxCashAdvance: Int?,
    @SerializedName("min_cash_advance") val minCashAdvance: Int?,
    @SerializedName("user_max_advance_amount") val userMaxAdvanceAmount: String?,
    @SerializedName("user_max_advance_formatted") val userMaxAdvanceFormatted: String?,
    @SerializedName("eligibility_status") val status: EligibilityStatusDto,
    @SerializedName("repayment_date") val repaymentDate: String?
) : Parcelable
