package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

interface AuthRepository {
    suspend fun refreshAuthToken(refreshTokenRequest: RefreshTokenRequest): Token
    suspend fun submitRegistrationIdentifiers(
        registrationIdentifiersRequest: RegistrationIdentifiersRequest
    )

    suspend fun validateCodeByIdentifier(
        identifiersCodeValidationRequest: IdentifiersCodeValidationRequest
    ): IdentifierToken

    suspend fun registerNewUser(
        createUserProfileRequest: CreateUserProfileRequest
    ): UserProfile

    suspend fun signInUser(
        signInRequest: SignInRequest
    ): UserProfile
}
