package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class SubmitRegistrationIdentifiersUseCase(private val repo: AuthRepository) :
    BaseUseCase<RegistrationIdentifiersRequest, Unit>() {
    override suspend fun run(params: RegistrationIdentifiersRequest): Result<Unit> =
        Result.Success(repo.submitRegistrationIdentifiers(params))
}
