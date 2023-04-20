package com.wolfpackdigital.cashli.domain.entities.response

data class Token(
    val accessToken: String,
    val tokenType: String,
    val expiresIn: Double,
    val refreshToken: String,
    val createdAt: Double
)
