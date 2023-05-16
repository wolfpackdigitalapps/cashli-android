package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.ChangePasswordRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class ChangePasswordUseCase(private val authRepo: AuthRepository) :
    BaseUseCase<ChangePasswordRequest, Unit>() {

    override suspend fun run(params: ChangePasswordRequest): Result<Unit> = Result.Success(
        authRepo.changePassword(params)
    )
}