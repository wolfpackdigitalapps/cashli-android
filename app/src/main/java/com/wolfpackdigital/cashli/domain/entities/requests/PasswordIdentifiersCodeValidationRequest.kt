package com.wolfpackdigital.cashli.domain.entities.requests

data class PasswordIdentifiersCodeValidationRequest(
    val identifier: String,
    val code: String
)