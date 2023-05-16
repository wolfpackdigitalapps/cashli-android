package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount

data class LinkBankAccountInfo(
    val eligibilityStatus: EligibilityStatus = EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED,
    val bankAccount: BankAccount? = null,
    val linkBankAccountAction: () -> Unit = {}
)
