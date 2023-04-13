package com.wolfpackdigital.cashli.domain.entities.requests

data class RefreshTokenRequest(
    val grantType: String,
    val refreshToken: String
)
