package com.wolfpackdigital.cashli.data.remote.dto.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastMembershipDto(
    @SerializedName("id") val id: Int,
    @SerializedName("start_date") val startDate: String,
    @SerializedName("end_date") val endDate: String,
    @SerializedName("status") val status: LastMembershipStatusDto,
    @SerializedName("amount") val amount: String,
    @SerializedName("membership_status") val membershipStatus: LastMembershipActivityStatusDto
) : Parcelable