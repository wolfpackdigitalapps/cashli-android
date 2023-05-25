package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetEligibilityStatusUseCase(private val repo: BankRepository) :
    BaseUseCase<Unit, EligibilityChecks>() {
    override suspend fun run(params: Unit): Result<EligibilityChecks> {
        return Result.Success(repo.getEligibilityStatus())
    }
}
