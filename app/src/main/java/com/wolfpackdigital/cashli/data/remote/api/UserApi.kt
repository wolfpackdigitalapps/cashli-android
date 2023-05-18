package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateIdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateUserProfileRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserSettingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserApi {
    @GET("v1/profile/users")
    suspend fun getUserProfile(): UserProfileDto

    @PATCH("v1/profile/user_settings")
    suspend fun updateUserProfileSetting(
        @Body userSetting: UserSettingDto
    ): UserSettingDto

    @POST("v1/profile/submit_identifiers")
    suspend fun submitChangeIdentifiers(
        @Body changeIdentifiersRequest: IdentifiersRequestDto
    )

    @PATCH("v1/profile/update_identifiers")
    suspend fun updateChangeIdentifiers(
        @Body updateIdentifiersRequest: UpdateIdentifiersCodeValidationRequestDto
    ): UserProfileDto

    @PATCH("v1/profile/users")
    suspend fun updateUserProfile(
        @Body updateUserProfileRequest: UpdateUserProfileRequestDto
    ): UserProfileDto
}
