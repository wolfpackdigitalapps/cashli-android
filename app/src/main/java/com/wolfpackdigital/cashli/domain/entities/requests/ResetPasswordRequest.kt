package com.wolfpackdigital.cashli.domain.entities.requests

data class ResetPasswordRequest(
    val token: String,
    val password: String,
    val confirmPassword: String
)