package com.wolfpackdigital.cashli.domain.entities.claimCash

data class TransferFees(
    val id: Int,
    val lowerLimitFormatted: String,
    val lowerLimit: Float,
    val upperLimitFormatted: String,
    val upperLimit: Float,
    val regularFee: String,
    val nextDayFee: String,
    val sameDayFee: String,
    val instantFee: String
)
