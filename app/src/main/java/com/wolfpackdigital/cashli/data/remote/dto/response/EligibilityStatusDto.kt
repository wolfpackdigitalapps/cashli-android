package com.wolfpackdigital.cashli.data.remote.dto.response

import com.google.gson.annotations.SerializedName

data class EligibilityStatusDto(
    @SerializedName("eligible") val eligible: Boolean? = null
)
