package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel

data class UpdateIdentifiersCodeValidationRequest(
    val channel: IdentifierChannel,
    val identifier: String,
    val code: String
)
