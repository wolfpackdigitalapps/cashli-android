package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class SubmitPasswordIdentifiersUseCase(private val repo: AuthRepository) :
    BaseUseCase<IdentifiersRequest, Unit>() {
    override suspend fun run(params: IdentifiersRequest): Result<Unit> =
        Result.Success(repo.submitPasswordIdentifiers(params))
}
