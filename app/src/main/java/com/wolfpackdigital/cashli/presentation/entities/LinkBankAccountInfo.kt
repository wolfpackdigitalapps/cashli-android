package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.response.BankAccount
import com.wolfpackdigital.cashli.presentation.entities.enums.BankAccountInfoType

data class LinkBankAccountInfo(
    val bankAccountInfoType: BankAccountInfoType = BankAccountInfoType.NOT_CONNECTED,
    val eligibilityStatus: EligibilityStatus = EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED,
    val bankAccount: BankAccount? = null,
    val linkBankAccountAction: () -> Unit = {}
)
