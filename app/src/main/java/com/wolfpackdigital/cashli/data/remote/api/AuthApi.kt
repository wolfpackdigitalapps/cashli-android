package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.response.TokenDto
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
//    @POST("auth/login")
//    suspend fun login(
//        @Query("emailAddress") emailAddress: String? = "",
//        @Query("password") password: String? = ""
//    ): Response<LoginResponse>

    @POST("v1/sessions/token")
    suspend fun refreshAuthToken(
        @Body refreshToken: RefreshTokenRequest
    ): TokenDto
}
