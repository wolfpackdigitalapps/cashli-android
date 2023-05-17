package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.IdentifiersRequestToIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateUserProfileRequestToUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingDtoToUserSettingMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingToUserSettingDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
    private val userSettingToDtoMapper: UserSettingToUserSettingDtoMapper,
    private val userSettingMapper: UserSettingDtoToUserSettingMapper,
    private val changeIdentifiersRequestMapper: IdentifiersRequestToIdentifiersRequestDtoMapper,
    private val updateIdentifiersRequestMapper: UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper,
    private val updateUserProfileRequestMapper: UpdateUserProfileRequestToUserProfileRequestDtoMapper
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val result = userApi.getUserProfile()
        return userProfileMapper.map(result)
    }

    override suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting {
        val result = userApi.updateUserProfileSetting(userSettingToDtoMapper.map(userSetting))
        return userSettingMapper.map(result)
    }

    override suspend fun submitChangeIdentifiers(changeIdentifiersRequest: IdentifiersRequest) {
        userApi.submitChangeIdentifiers(changeIdentifiersRequestMapper.map(changeIdentifiersRequest))
    }

    override suspend fun updateChangeIdentifiers(updateIdentifiersRequest: UpdateIdentifiersCodeValidationRequest): UserProfile {
        val result = userApi.updateChangeIdentifiers(
            updateIdentifiersRequestMapper.map(updateIdentifiersRequest)
        )
        return userProfileMapper.map(result)
    }

    override suspend fun updateUserProfile(updateUserProfileRequest: UpdateUserProfileRequest): UserProfile {
        val result = userApi.updateUserProfile(
            updateUserProfileRequestMapper.map(updateUserProfileRequest)
        )
        return userProfileMapper.map(result)
    }
}
