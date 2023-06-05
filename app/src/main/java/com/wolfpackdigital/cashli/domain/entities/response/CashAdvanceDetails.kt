package com.wolfpackdigital.cashli.domain.entities.response

import com.wolfpackdigital.cashli.domain.entities.enums.CashAdvanceStatus

data class CashAdvanceDetails(
    val amount: String,
    val tip: String,
    val payoutFee: String,
    val totalRepayable: String,
    val cashAdvanceStatus: CashAdvanceStatus,
    val dueDate: String,
    val paidDate: String
)
