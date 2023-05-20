package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class UnlinkAccountUseCase(private val repo: BankRepository) : BaseUseCase<Unit, Unit>() {

    override suspend fun run(params: Unit): Result<Unit> = Result.Success(repo.unlinkAccount())
}