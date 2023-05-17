package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.response.BankTransactionDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import com.wolfpackdigital.cashli.data.remote.dto.response.UserSettingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
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
}
