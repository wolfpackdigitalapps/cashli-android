package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.CreateUserProfileRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.RefreshTokenRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.ResetPasswordRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.SignInRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.SingleDataRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.IdentifierTokenDto
import com.wolfpackdigital.cashli.data.remote.dto.response.PasswordIdentifierTokenDto
import com.wolfpackdigital.cashli.data.remote.dto.response.TokenDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("v1/registration/submit_identifiers")
    suspend fun submitRegistrationIdentifiers(
        @Body registrationIdentifiersRequest: IdentifiersRequestDto
    )

    @POST("v1/registration/validate_codes")
    suspend fun validateCodeByIdentifier(
        @Body identifiersCodeValidationRequest: IdentifiersCodeValidationRequestDto
    ): IdentifierTokenDto

    @POST("v1/sessions/token")
    suspend fun refreshAuthToken(
        @Body refreshToken: RefreshTokenRequestDto
    ): TokenDto

    @POST("v1/registration/users")
    suspend fun registerNewUser(
        @Body createUserProfileRequest: CreateUserProfileRequestDto
    ): UserProfileDto

    @POST("v1/sessions")
    suspend fun signInUser(
        @Body signInRequest: SignInRequestDto
    ): UserProfileDto

    @POST("v1/password/submit_identifiers")
    suspend fun submitPasswordIdentifiers(
        @Body passwordIdentifiersRequest: IdentifiersRequestDto
    )

    @POST("v1/password/validate_codes")
    suspend fun validateCodeByPasswordIdentifier(
        @Body passwordIdentifiersCodeValidationRequest: IdentifiersCodeValidationRequestDto
    ): PasswordIdentifierTokenDto

    @POST("v1/password/reset")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequestDto
    )

    @POST("v1/device_tokens")
    suspend fun registerDeviceToken(
        @Body singleDataRequest: SingleDataRequestDto
    )
}
