package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.CashAdvanceDetailsDtoToCashAdvanceDetailsMapper
import com.wolfpackdigital.cashli.data.mappers.CashAdvanceRequestToCashAdvanceRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.TransferFeesDtoToTransferFeesMapper
import com.wolfpackdigital.cashli.data.remote.api.CashAdvanceApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.PaginationRequest
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails

class CashAdvanceRepositoryImpl(
    private val cashAdvanceApi: CashAdvanceApi,
    private val transferFeesMapper: TransferFeesDtoToTransferFeesMapper,
    private val cashAdvanceMapper: CashAdvanceRequestToCashAdvanceRequestDtoMapper,
    private val cashAdvanceDetailsMapper: CashAdvanceDetailsDtoToCashAdvanceDetailsMapper
) : CashAdvanceRepository {
    override suspend fun getCashAdvancesHistory(paginationRequest: PaginationRequest): List<CashAdvanceDetails> {
        val result = cashAdvanceApi.getCashAdvancesHistory(
            paginationRequest.page, paginationRequest.perPage
        )
        return result.map { cashAdvanceDetailsMapper.map(it) }
    }

    override suspend fun getTransferFees() =
        cashAdvanceApi.getTransferFees().map { transferFeesMapper.map(it) }

    override suspend fun requestCashAdvance(request: CashAdvanceRequest): CashAdvanceDetails {
        val result = cashAdvanceApi.requestCashAdvance(cashAdvanceMapper.map(request))
        return cashAdvanceDetailsMapper.map(result)
    }
}
