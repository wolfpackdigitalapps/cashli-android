package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

data class IdentifiersTokenRequest(
    val emailToken: String = EMPTY_STRING,
    val phoneNumberToken: String = EMPTY_STRING
)
