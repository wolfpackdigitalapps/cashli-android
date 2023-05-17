package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class UpdateUserProfileUseCase(private val repo: UserRepository) :
    BaseUseCase<UpdateUserProfileRequest, UserProfile>() {

    override suspend fun run(params: UpdateUserProfileRequest): Result<UserProfile> =
        Result.Success(repo.updateUserProfile(params))
}