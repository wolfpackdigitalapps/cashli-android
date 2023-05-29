package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOutstandingBalanceStatusDto(
    @SerializedName("outstanding_balance") val outstandingBalance: Boolean
) : Parcelable
