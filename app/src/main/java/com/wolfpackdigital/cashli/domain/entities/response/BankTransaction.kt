package com.wolfpackdigital.cashli.domain.entities.response

data class BankTransaction(
    val transactionId: Int,
    val date: String,
    val merchantName: String,
    val amount: String
)
