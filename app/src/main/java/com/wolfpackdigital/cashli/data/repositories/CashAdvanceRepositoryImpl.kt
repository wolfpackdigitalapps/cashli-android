package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.TransferFeesDtoToTransferFeesMapper
import com.wolfpackdigital.cashli.data.remote.api.CashAdvanceApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository

class CashAdvanceRepositoryImpl(
    private val cashAdvanceApi: CashAdvanceApi,
    private val transferFeesMapper: TransferFeesDtoToTransferFeesMapper
) : CashAdvanceRepository {

    override suspend fun getTransferFees() =
        cashAdvanceApi.getTransferFees().map { transferFeesMapper.map(it) }
}
