package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserSettingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserApi {
    @GET("v1/profile/users")
    suspend fun getUserProfile(): UserProfileDto

    @PATCH("v1/profile/user_settings")
    suspend fun updateUserProfileSetting(
        @Body userSetting: UserSettingDto
    ): UserSettingDto
}
