package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class RegisterNewUserUseCase(private val repo: AuthRepository) :
    BaseUseCase<CreateUserProfileRequest, UserProfile>() {
    override suspend fun run(params: CreateUserProfileRequest): Result<UserProfile> =
        Result.Success(repo.registerNewUser(params))
}
