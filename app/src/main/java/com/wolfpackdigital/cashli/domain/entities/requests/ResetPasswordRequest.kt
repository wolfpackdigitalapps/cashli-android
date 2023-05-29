package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.Language

data class ResetPasswordRequest(
    val token: String,
    val password: String,
    val confirmPassword: String,
    val locale: Language
)
