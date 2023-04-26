package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class SignInUserUseCase(private val repo: AuthRepository) :
    BaseUseCase<SignInRequest, UserProfile>() {
    override suspend fun run(params: SignInRequest): Result<UserProfile> =
        Result.Success(repo.signInUser(params))
}
