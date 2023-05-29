package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.Language

data class UpdateUserProfileRequest(
    val firstName: String,
    val lastName: String,
    val street: String,
    val zipCode: String,
    val city: String,
    val state: String,
    val language: Language = Language.ENGLISH
)
