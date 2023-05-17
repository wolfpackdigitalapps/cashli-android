package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class ValidateCodeByUpdateIdentifiersUseCase(private val repo: UserRepository) :
    BaseUseCase<UpdateIdentifiersCodeValidationRequest, UserProfile>() {
    override suspend fun run(params: UpdateIdentifiersCodeValidationRequest): Result<UserProfile> =
        Result.Success(repo.updateChangeIdentifiers(params))
}