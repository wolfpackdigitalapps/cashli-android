package com.wolfpackdigital.cashli.presentation.entities

data class RequestCashAdvanceInfo(
    var cashApproved: Float? = null,
    var cashAdvanceBalance: Float? = null,
    var upToSum: String? = null,
    var repaymentDate: String? = null,
    var eligible: Boolean = false,
    var seeMoreAction: () -> Unit = {},
    var claimCashNowAction: () -> Unit = {}
)
