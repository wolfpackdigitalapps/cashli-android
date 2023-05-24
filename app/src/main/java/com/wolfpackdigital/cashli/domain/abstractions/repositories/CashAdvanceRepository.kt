package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees

interface CashAdvanceRepository {
    suspend fun getTransferFees(): List<TransferFees>
}
