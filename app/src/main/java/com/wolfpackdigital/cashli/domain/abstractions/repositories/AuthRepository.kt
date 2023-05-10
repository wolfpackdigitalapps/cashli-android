package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.requests.ResetPasswordRequest
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.domain.entities.response.PasswordIdentifierToken
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

interface AuthRepository {
    suspend fun refreshAuthToken(refreshTokenRequest: RefreshTokenRequest): Token
    suspend fun submitRegistrationIdentifiers(
        identifiersRequest: IdentifiersRequest
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

    suspend fun submitPasswordIdentifiers(
        passwordIdentifiersRequest: IdentifiersRequest
    )

    suspend fun validateCodeByPasswordIdentifier(
        passwordIdentifiersCodeValidationRequest: IdentifiersCodeValidationRequest
    ): PasswordIdentifierToken

    suspend fun resetPassword(
        resetPasswordRequest: ResetPasswordRequest
    )

    suspend fun signOutUser()
}
