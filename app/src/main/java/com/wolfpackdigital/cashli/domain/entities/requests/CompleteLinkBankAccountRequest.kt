package com.wolfpackdigital.cashli.domain.entities.requests

data class CompleteLinkBankAccountRequest(
    val publicToken: String,
    val metadata: Any
)
