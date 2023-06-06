package com.wolfpackdigital.cashli.domain.entities.response

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus

data class EligibilityChecks(
    val maxCashAdvance: Int,
    val minCashAdvance: Int,
    val userMaxAdvanceAmount: String,
    val userMaxAdvanceFormatted: String,
    val status: EligibilityStatus
)
