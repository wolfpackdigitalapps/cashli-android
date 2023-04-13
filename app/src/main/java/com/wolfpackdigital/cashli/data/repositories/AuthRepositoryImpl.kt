package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.TokenDtoToTokenMapper
import com.wolfpackdigital.cashli.data.remote.api.AuthApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenMapper: TokenDtoToTokenMapper
) : AuthRepository {
    override suspend fun refreshAuthToken(refreshTokenRequest: RefreshTokenRequest) =
        tokenMapper.map(
            authApi.refreshAuthToken(refreshTokenRequest)
        )
}
