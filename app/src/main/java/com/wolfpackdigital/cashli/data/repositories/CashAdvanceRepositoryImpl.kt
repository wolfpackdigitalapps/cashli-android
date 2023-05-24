package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.CashAdvanceRequestToCashAdvanceRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TransferFeesDtoToTransferFeesMapper
import com.wolfpackdigital.cashli.data.remote.api.CashAdvanceApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest

class CashAdvanceRepositoryImpl(
    private val cashAdvanceApi: CashAdvanceApi,
    private val transferFeesMapper: TransferFeesDtoToTransferFeesMapper,
    private val cashAdvanceMapper: CashAdvanceRequestToCashAdvanceRequestDtoMapper
) : CashAdvanceRepository {

    override suspend fun getTransferFees() =
        cashAdvanceApi.getTransferFees().map { transferFeesMapper.map(it) }

    override suspend fun requestCashAdvance(request: CashAdvanceRequest) =
        cashAdvanceApi.requestCashAdvance(cashAdvanceMapper.map(request))
}
