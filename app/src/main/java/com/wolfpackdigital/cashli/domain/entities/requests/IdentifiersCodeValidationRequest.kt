package com.wolfpackdigital.cashli.domain.entities.requests

data class IdentifiersCodeValidationRequest(
    val identifier: String,
    val code: String
)
