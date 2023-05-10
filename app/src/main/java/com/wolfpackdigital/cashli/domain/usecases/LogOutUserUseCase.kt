package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class LogOutUserUseCase(private val repo: AuthRepository) :
    BaseUseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): Result<Unit> =
        Result.Success(repo.signOutUser())
}