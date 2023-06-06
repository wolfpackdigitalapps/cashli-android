package com.wolfpackdigital.cashli.domain.entities.response

import com.wolfpackdigital.cashli.domain.entities.enums.BankAccountSubtype

data class BankAccount(
    val institutionName: String,
    val name: String,
    val accountSubtype: BankAccountSubtype,
    val accountNumberMask: String,
    val balance: String,
    val relinkableAt: String?,
    val balanceRefreshedAt: String?
)
