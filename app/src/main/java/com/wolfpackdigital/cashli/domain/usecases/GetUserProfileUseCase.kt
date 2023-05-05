package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetUserProfileUseCase(private val repo: UserRepository) :
    BaseUseCase<Unit, UserProfile>() {
    override suspend fun run(params: Unit): Result<UserProfile> {
        return Result.Success(repo.getUserProfile())
    }
}
