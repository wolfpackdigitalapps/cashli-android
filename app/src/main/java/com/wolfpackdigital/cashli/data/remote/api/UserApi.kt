package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.UserSettingDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateIdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateUserProfileRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.BankTransactionDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query

interface UserApi {
    @GET("v1/profile/users")
    suspend fun getUserProfile(): UserProfileDto

    @PATCH("v1/profile/user_settings")
    suspend fun updateUserProfileSetting(
        @Body userSetting: UserSettingDto
    ): UserSettingDto

    @GET("v1/profile/transactions")
    suspend fun getUserBankTransaction(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<BankTransactionDto>

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

    @POST("v1/profile/pause")
    suspend fun pauseUserAccount()

    @POST("v1/profile/unpause")
    suspend fun unpauseUserAccount()
}
