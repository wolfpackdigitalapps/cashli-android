package com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount

data class LinkAccountLocalizedBalanceRequest(
    val available: String? = null,
    val current: String? = null,
)
