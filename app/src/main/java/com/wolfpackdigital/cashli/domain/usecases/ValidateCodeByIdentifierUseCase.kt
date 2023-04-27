package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class ValidateCodeByIdentifierUseCase(private val repo: AuthRepository) :
    BaseUseCase<IdentifiersCodeValidationRequest, IdentifierToken>() {
    override suspend fun run(params: IdentifiersCodeValidationRequest): Result<IdentifierToken> =
        Result.Success(repo.validateCodeByIdentifier(params))
}
