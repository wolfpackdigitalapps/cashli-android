package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class GetTransferFeesUseCase(private val cashAdvanceRepository: CashAdvanceRepository) :
    BaseUseCase<Unit, List<TransferFees>>() {

    override suspend fun run(params: Unit): Result<List<TransferFees>> {
        return Result.Success(cashAdvanceRepository.getTransferFees())
    }
}
