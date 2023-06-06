package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetCashAdvancesLimitsUseCase(private val repo: CashAdvanceRepository) :
    BaseUseCase<Unit, EligibilityChecks>() {
    override suspend fun run(params: Unit): Result<EligibilityChecks> {
        return Result.Success(repo.getCashAdvancesLimits())
    }
}
