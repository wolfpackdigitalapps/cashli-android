package com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount

data class LinkAccountSubtypeRequest(
    val name: String? = null,
    val type: LinkAccountTypeRequest? = null
)
