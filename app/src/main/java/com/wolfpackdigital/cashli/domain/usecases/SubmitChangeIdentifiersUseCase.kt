package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class SubmitChangeIdentifiersUseCase(private val repo: UserRepository) :
    BaseUseCase<IdentifiersRequest, Unit>() {

    override suspend fun run(params: IdentifiersRequest): Result<Unit> =
        Result.Success(repo.submitChangeIdentifiers(params))
}
