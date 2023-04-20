package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.RefreshTokenRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.RegistrationIdentifiersRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.TokenDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("v1/registration/submit_identifiers")
    suspend fun submitRegistrationIdentifiers(
        @Body channel: RegistrationIdentifiersRequestDto
    )

    @POST("v1/sessions/token")
    suspend fun refreshAuthToken(
        @Body refreshToken: RefreshTokenRequestDto
    ): TokenDto
}
