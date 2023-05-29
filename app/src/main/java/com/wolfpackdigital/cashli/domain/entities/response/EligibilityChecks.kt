package com.wolfpackdigital.cashli.domain.entities.response

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus

data class EligibilityChecks(
    val status: EligibilityStatus
)
