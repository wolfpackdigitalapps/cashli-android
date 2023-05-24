package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.BankTokenDtoToBankTokenMapper
import com.wolfpackdigital.cashli.data.mappers.CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.EligibilityStatusDtoToEligibilityStatusMapper
import com.wolfpackdigital.cashli.data.remote.api.BankApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankToken

@Suppress("MaxLineLength")
class BankRepositoryImpl(
    private val bankApi: BankApi,
    private val bankTokenMapper: BankTokenDtoToBankTokenMapper,
    private val eligibilityStatusMapper: EligibilityStatusDtoToEligibilityStatusMapper,
    private val bankAccountRequestMapper: CompleteLinkBankAccountRequestToCompleteLinkBankAccountRequestDtoMapper
) : BankRepository {
    override suspend fun getEligibilityStatus(): EligibilityStatus {
        val result = bankApi.getEligibilityStatus()
        return eligibilityStatusMapper.map(result)
    }

    override suspend fun generateLinkToken(): BankToken {
        val result = bankApi.generateLinkToken()
        return bankTokenMapper.map(result)
    }

    override suspend fun completeLinkingBankAccount(linkBankAccountDtoRequest: CompleteLinkBankAccountRequest) {
        val dtoRequest = bankAccountRequestMapper.map(linkBankAccountDtoRequest)
        bankApi.completeLinkingBankAccount(dtoRequest)
    }

    override suspend fun generateUpdateLinkToken(): BankToken {
        val result = bankApi.generateUpdateLinkToken()
        return bankTokenMapper.map(result)
    }

    override suspend fun completeUpdateLinkingBankAccount(updateLinkBankAccountDtoRequest: CompleteLinkBankAccountRequest) {
        val dtoRequest = bankAccountRequestMapper.map(updateLinkBankAccountDtoRequest)
        bankApi.completeUpdateLinkingBankAccount(dtoRequest)
    }
}
