package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.Languages
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

data class UserProfileRequest(
    val firstName: String = EMPTY_STRING,
    val lastName: String = EMPTY_STRING,
    val email: String = EMPTY_STRING,
    val password: String = EMPTY_STRING,
    val phoneNumber: String = EMPTY_STRING,
    val street: String = EMPTY_STRING,
    val zipCode: String = EMPTY_STRING,
    val city: String = EMPTY_STRING,
    val state: String = EMPTY_STRING,
    val language: Languages = Languages.ENGLISH
)
