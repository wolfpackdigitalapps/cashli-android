package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.ResetPasswordRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class ResetPasswordUseCase(private val repo: AuthRepository) :
    BaseUseCase<ResetPasswordRequest, Unit>() {
    override suspend fun run(params: ResetPasswordRequest): Result<Unit> =
        Result.Success(repo.resetPassword(params))
}
