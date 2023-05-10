package com.wolfpackdigital.cashli.data.remote.dto.enums

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
enum class BankAccountSubtypeDto : Parcelable {
    @SerializedName("checking")
    CHECKING,

    @SerializedName("depository")
    DEPOSITORY,

    @SerializedName("savings")
    SAVINGS
}
