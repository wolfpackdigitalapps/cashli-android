package com.wolfpackdigital.cashli.domain.entities.requests

data class UserSignInRequest(
    val identifier: String,
    val password: String
)