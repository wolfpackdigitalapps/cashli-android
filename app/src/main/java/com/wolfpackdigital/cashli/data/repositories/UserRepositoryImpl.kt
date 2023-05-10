package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingDtoToUserSettingMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingToUserSettingDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
    private val userSettingToDtoMapper: UserSettingToUserSettingDtoMapper,
    private val userSettingMapper: UserSettingDtoToUserSettingMapper
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val result = userApi.getUserProfile()
        return userProfileMapper.map(result)
    }

    override suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting {
        val result = userApi.updateUserProfileSetting(userSettingToDtoMapper.map(userSetting))
        return userSettingMapper.map(result)
    }
}
