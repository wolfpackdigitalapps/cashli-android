package com.wolfpackdigital.cashli.domain.entities.requests

import com.wolfpackdigital.cashli.domain.entities.enums.RegistrationIdentifierChannel

data class RegistrationIdentifiersRequest(
    val channel: RegistrationIdentifierChannel,
    val identifier: String
)
