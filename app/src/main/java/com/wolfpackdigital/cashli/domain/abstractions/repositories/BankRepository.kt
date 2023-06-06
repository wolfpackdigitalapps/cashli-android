package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken

interface BankRepository {
    suspend fun generateLinkToken(): BankToken
    suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest)
    suspend fun unlinkAccount()
    suspend fun generateUpdateLinkToken(): BankToken
    suspend fun completeUpdateLinkingBankAccount(updateLinkBankAccountDtoRequest: CompleteLinkBankAccountRequest)
}
