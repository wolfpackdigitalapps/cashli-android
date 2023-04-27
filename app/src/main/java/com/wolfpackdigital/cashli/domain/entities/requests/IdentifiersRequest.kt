package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel

data class IdentifiersRequest(
    val channel: IdentifierChannel,
    val identifier: String
)
