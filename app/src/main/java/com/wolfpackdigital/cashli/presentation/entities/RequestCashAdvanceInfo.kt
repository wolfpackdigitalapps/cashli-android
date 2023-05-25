package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.presentation.entities.enums.RequestCashAdvanceType

data class RequestCashAdvanceInfo(
    val cashApproved: String? = null,
    val cashAdvanceBalance: String? = null,
    val upToSum: String? = null,
    val repaymentDate: String? = null,
    val requestCashAdvanceType: RequestCashAdvanceType = RequestCashAdvanceType.CASH_UP_TO,
    val eligibilityStatus: EligibilityStatus = EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED,
    val isClaimCashEnabled: Boolean = true,
    val seeMoreAction: () -> Unit = {},
    val claimCashNowAction: () -> Unit = {}
)
