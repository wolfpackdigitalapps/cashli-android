package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.Language

data class IdentifiersCodeValidationRequest(
    val identifier: String,
    val code: String,
    val locale: Language
)
