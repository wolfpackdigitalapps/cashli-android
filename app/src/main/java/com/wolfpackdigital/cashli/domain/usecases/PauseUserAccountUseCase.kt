package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class PauseUserAccountUseCase(private val repo: UserRepository) :
    BaseUseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): Result<Unit> =
        Result.Success(repo.pauseUserAccount())
}
