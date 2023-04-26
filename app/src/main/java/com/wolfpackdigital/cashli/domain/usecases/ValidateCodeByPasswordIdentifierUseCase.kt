package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.response.PasswordIdentifierToken
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class ValidateCodeByPasswordIdentifierUseCase(private val repo: AuthRepository) :
    BaseUseCase<IdentifiersCodeValidationRequest, PasswordIdentifierToken>() {
    override suspend fun run(params: IdentifiersCodeValidationRequest): Result<PasswordIdentifierToken> =
        Result.Success(repo.validateCodeByPasswordIdentifier(params))
}
