package com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount

data class LinkAccountMetadataRequest(
    val accounts: List<LinkAccountInfoRequest>? = null,
    val institution: LinkInstitutionRequest? = null,
    val linkSessionId: String? = null,
    val metadataJson: String? = null
)
