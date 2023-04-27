package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

private const val GRANT_TYPE = "refresh_token"

class RefreshTokenUseCase(private val repo: AuthRepository) :
    BaseUseCase<String, Token>() {
    override suspend fun run(params: String): Result<Token> {
        val request = RefreshTokenRequest(GRANT_TYPE, params)
        return Result.Success(repo.refreshAuthToken(request))
    }
}
