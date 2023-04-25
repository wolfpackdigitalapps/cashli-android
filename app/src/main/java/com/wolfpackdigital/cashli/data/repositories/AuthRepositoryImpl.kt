package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.CreateUserProfileRequestToCreateUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifierTokenDtoToIdentifierTokenMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersCodeValidationRequestToIdentifiersCodeValidationRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RefreshTokenRequestToRefreshTokenRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.SignInRequestToSignInRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TokenDtoToTokenMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.remote.api.AuthApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

@Suppress("MaxLineLength", "LongParameterList")
class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val tokenMapper: TokenDtoToTokenMapper,
    private val refreshTokenMapper: RefreshTokenRequestToRefreshTokenRequestDtoMapper,
    private val registrationIdentifiersRequestMapper: RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper,
    private val identifiersCodeValidationRequestMapper: IdentifiersCodeValidationRequestToIdentifiersCodeValidationRequestDtoMapper,
    private val identifierTokenMapper: IdentifierTokenDtoToIdentifierTokenMapper,
    private val createUserProfileRequestMapper: CreateUserProfileRequestToCreateUserProfileRequestDtoMapper,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
    private val signInRequestMapper: SignInRequestToSignInRequestDtoMapper
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

    override suspend fun validateCodeByIdentifier(identifiersCodeValidationRequest: IdentifiersCodeValidationRequest): IdentifierToken {
        val request = identifiersCodeValidationRequestMapper.map(identifiersCodeValidationRequest)
        val result = authApi.validateCodeByIdentifier(request)
        return identifierTokenMapper.map(result)
    }

    override suspend fun registerNewUser(createUserProfileRequest: CreateUserProfileRequest): UserProfile {
        val request = createUserProfileRequestMapper.map(createUserProfileRequest)
        val result = authApi.registerNewUser(request)
        return userProfileMapper.map(result)
    }

    override suspend fun signInUser(signInRequest: SignInRequest): UserProfile {
        val request = signInRequestMapper.map(signInRequest)
        val result = authApi.signInUser(request)
        return userProfileMapper.map(result)
    }
}
