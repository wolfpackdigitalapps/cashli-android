package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.response.BankToken
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GenerateUpdateLinkTokenUseCase(private val repo: BankRepository) :
    BaseUseCase<Unit, BankToken>() {

    override suspend fun run(params: Unit): Result<BankToken> =
        Result.Success(repo.generateUpdateLinkToken())
}
