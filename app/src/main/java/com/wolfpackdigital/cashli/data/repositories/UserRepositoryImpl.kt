package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val result = userApi.getUserProfile()
        return userProfileMapper.map(result)
    }
}
