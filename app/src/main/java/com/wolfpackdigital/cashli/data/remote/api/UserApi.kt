package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import retrofit2.http.GET

interface UserApi {
    @GET("v1/profile/users")
    suspend fun getUserProfile(): UserProfileDto
}
