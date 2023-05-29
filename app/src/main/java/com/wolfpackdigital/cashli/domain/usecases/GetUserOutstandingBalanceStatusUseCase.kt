package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.response.UserOutstandingBalanceStatus
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetUserOutstandingBalanceStatusUseCase(private val repo: UserRepository) :
    BaseUseCase<Unit, UserOutstandingBalanceStatus>() {

    override suspend fun run(params: Unit): Result<UserOutstandingBalanceStatus> =
        Result.Success(repo.getUserOutstandingBalanceStatus())
}
