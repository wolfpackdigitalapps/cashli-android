package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken

interface BankRepository {
    suspend fun getEligibilityStatus(): EligibilityStatus
    suspend fun generateLinkToken(): BankToken
    suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest)
}
