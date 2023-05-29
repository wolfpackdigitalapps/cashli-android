package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class CloseUserAccountUseCase(private val repo: UserRepository) :
    BaseUseCase<CloseUserAccountReasonRequest, Unit>() {

    override suspend fun run(params: CloseUserAccountReasonRequest): Result<Unit> =
        Result.Success(repo.closeUserAccount(params))
}
