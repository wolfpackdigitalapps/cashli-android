package com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount

data class LinkAccountInfoRequest(
    val id: String? = null,
    val name: String? = null,
    val mask: String? = null,
    val subtype: LinkAccountSubtypeRequest? = null,
    val verificationStatus: LinkAccountVerificationStatusRequest? = null,
    val balance: LinkAccountBalanceRequest? = null
)
