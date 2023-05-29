package com.wolfpackdigital.cashli.data.remote.dto.requests

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CloseUserAccountReasonRequestDto(
    @SerializedName("reason") val reason: String?
) : Parcelable
