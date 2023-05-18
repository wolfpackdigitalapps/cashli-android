package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class EligibilityStatusDto : Parcelable {
    @SerializedName("bank_account_not_connected")
    BANK_ACCOUNT_NOT_CONNECTED,

    @SerializedName("eligibility_check_pending")
    ELIGIBILITY_CHECK_PENDING,

    @SerializedName("eligible")
    ELIGIBLE,

    @SerializedName("not_eligible")
    NOT_ELIGIBLE
}
