package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.BankTokenDtoToBankTokenMapper
import com.wolfpackdigital.cashli.data.mappers.CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.EligibilityChecksDtoToEligibilityChecksMapper
import com.wolfpackdigital.cashli.data.remote.api.BankApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks

@Suppress("MaxLineLength")
class BankRepositoryImpl(
    private val bankApi: BankApi,
    private val bankTokenMapper: BankTokenDtoToBankTokenMapper,
    private val eligibilityChecksMapper: EligibilityChecksDtoToEligibilityChecksMapper,
    private val bankAccountRequestMapper: CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper
) : BankRepository {
    override suspend fun getEligibilityStatus(): EligibilityChecks {
        val result = bankApi.getEligibilityStatus()
        return eligibilityChecksMapper.map(result)
    }

    override suspend fun generateLinkToken(): BankToken {
        val result = bankApi.generateLinkToken()
        return bankTokenMapper.map(result)
    }

    override suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest) {
        val dtoRequest = bankAccountRequestMapper.map(linkBankAccountDtoRequest)
        bankApi.completeLinkingBankAccount(dtoRequest)
    }
}
