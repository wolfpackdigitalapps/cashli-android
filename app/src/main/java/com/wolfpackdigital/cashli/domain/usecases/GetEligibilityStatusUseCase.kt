package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityStatus
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetEligibilityStatusUseCase(private val repo: BankRepository) :
    BaseUseCase<Unit, EligibilityStatus>() {
    override suspend fun run(params: Unit): Result<EligibilityStatus> {
        return Result.Success(repo.getEligibilityStatus())
    }
}
