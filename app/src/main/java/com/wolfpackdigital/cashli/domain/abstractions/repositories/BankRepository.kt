package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks

interface BankRepository {
    suspend fun getEligibilityStatus(): EligibilityChecks
    suspend fun generateLinkToken(): BankToken
    suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest)
}
