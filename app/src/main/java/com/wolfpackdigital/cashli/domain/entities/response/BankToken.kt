package com.wolfpackdigital.cashli.domain.entities.response

data class BankToken(
    val linkToken: String,
    val expiration: String,
    val redirectUri: String
)
