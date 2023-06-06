package com.wolfpackdigital.cashli.domain.entities.response

data class UserOutstandingBalanceStatus(
    val outstandingBalance: Boolean,
    val cashAdvanceBalanceDue: String?,
    val repaymentDate: String?
)
