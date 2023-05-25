package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest

interface CashAdvanceRepository {
    suspend fun getTransferFees(): List<TransferFees>
    suspend fun requestCashAdvance(request: CashAdvanceRequest)
}
