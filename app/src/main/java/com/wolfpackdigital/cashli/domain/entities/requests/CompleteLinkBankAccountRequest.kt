package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountMetadataRequest

data class CompleteLinkBankAccountRequest(
    val publicToken: String,
    val metadata: LinkAccountMetadataRequest
)
