package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.response.Token

interface AuthRepository {
    suspend fun refreshAuthToken(refreshTokenRequest: RefreshTokenRequest): Token
    suspend fun submitRegistrationIdentifiers(registrationIdentifiersRequest: RegistrationIdentifiersRequest)
}
