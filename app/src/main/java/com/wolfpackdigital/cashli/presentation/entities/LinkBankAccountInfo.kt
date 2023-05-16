package com.wolfpackdigital.cashli.presentation.entities

import com.wolfpackdigital.cashli.domain.entities.response.BankAccount

data class LinkBankAccountInfo(
    var isPending: Boolean = false,
    var bankAccount: BankAccount? = null,
    var linkBankAccountAction: () -> Unit = {}
) {
    fun onLinkBankAccountAction() = linkBankAccountAction()
}
