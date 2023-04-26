package com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount

data class LinkAccountBalanceRequest(
    val available: Double? = null,
    val currency: String? = null,
    val current: Double? = null,
    val localized: LinkAccountLocalizedBalanceRequest? = null
)
