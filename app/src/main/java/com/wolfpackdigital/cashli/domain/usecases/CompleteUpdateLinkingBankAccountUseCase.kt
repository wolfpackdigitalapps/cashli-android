package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.BankRepository
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class CompleteUpdateLinkingBankAccountUseCase(private val repo: BankRepository) :
    BaseUseCase<CompleteLinkBankAccountRequest, Unit>() {

    override suspend fun run(params: CompleteLinkBankAccountRequest): Result<Unit> =
        Result.Success(repo.completeUpdateLinkingBankAccount(params))
}