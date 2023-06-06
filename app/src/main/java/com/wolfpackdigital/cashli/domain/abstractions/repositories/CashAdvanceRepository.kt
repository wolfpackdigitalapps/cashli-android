package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.claimCash.TransferFees
import com.wolfpackdigital.cashli.domain.entities.requests.CashAdvanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.PaginationRequest
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks

interface CashAdvanceRepository {
    suspend fun getCashAdvancesLimits(): EligibilityChecks
    suspend fun getCashAdvancesHistory(paginationRequest: PaginationRequest): List<CashAdvanceDetails>
    suspend fun getTransferFees(): List<TransferFees>
    suspend fun requestCashAdvance(request: CashAdvanceRequest): CashAdvanceDetails
}
