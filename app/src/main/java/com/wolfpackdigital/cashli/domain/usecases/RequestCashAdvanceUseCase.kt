package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class RequestCashAdvanceUseCase(private val repo: CashAdvanceRepository) :
    BaseUseCase<CashAdvanceRequest, CashAdvanceDetails>() {

    override suspend fun run(params: CashAdvanceRequest) =
        Result.Success(repo.requestCashAdvance(params))
}
