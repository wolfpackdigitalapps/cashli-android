package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.Language

data class SignInRequest(
    val userSignInRequest: UserSignInRequest,
    val locale: Language
)
