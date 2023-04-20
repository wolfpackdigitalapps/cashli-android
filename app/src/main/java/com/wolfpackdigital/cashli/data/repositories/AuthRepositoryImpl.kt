package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestToRefreshTokenRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TokenDtoToTokenMapper
import com.wolfpackdigital.cashli.data.remote.api.AuthApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.response.Token

@Suppress("MaxLineLength")
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenMapper: TokenDtoToTokenMapper,
    private val refreshTokenMapper: RefreshTokenRequestToRefreshTokenRequestDtoMapper,
    private val registrationIdentifiersRequestMapper: RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper
) : AuthRepository {
    override suspend fun refreshAuthToken(refreshTokenRequest: RefreshTokenRequest): Token {
        val requestDto = refreshTokenMapper.map(refreshTokenRequest)
        val result = authApi.refreshAuthToken(requestDto)
        return tokenMapper.map(result)
    }

    override suspend fun submitRegistrationIdentifiers(registrationIdentifiersRequest: RegistrationIdentifiersRequest) {
        authApi.submitRegistrationIdentifiers(
            registrationIdentifiersRequestMapper.map(
                registrationIdentifiersRequest
            )
        )
    }
}
