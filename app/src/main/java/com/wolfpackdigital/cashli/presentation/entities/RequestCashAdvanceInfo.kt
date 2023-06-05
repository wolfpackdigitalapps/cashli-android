package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.presentation.entities.enums.RequestCashAdvanceType

data class RequestCashAdvanceInfo(
    val cashApproved: String? = null,
    val cashAdvanceBalance: String? = null,
    val upToSum: String? = null,
    val eligibilityDate: String? = null,
    val repaymentDate: String? = null,
    val requestCashAdvanceType: RequestCashAdvanceType = RequestCashAdvanceType.CASH_UP_TO,
    val isActionButtonEnabled: Boolean = true,
    val isAccountPaused: Boolean = false,
    val warningInfo: GenericWarningInfo? = null,
    val buttonAction: () -> Unit = {}
)
