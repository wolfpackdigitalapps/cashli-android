package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityStatus

interface BankRepository {
    suspend fun getEligibilityStatus(): EligibilityStatus
    suspend fun generateLinkToken(): BankToken
    suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest)
}
