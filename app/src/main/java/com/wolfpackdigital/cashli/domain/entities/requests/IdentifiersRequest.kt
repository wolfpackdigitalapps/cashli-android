package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.enums.Language

data class IdentifiersRequest(
    val channel: IdentifierChannel,
    val identifier: String,
    val locale: Language
)
