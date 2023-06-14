package com.wolfpackdigital.cashli.data.remote.api.common

import com.wolfpackdigital.cashli.data.mappers.TokenToTokenDtoMapper
import com.wolfpackdigital.cashli.domain.usecases.RefreshTokenUseCase
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.exceptions.RefreshTokenExpiredException
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.HttpURLConnection

private const val CHANGE_PASSWORD_LAST_SEGMENT = "change"
private const val MAX_RETRY_COUNT = 2

class RefreshTokenInterceptor : Authenticator, KoinComponent, PersistenceService {
    private val refreshTokenUseCase: RefreshTokenUseCase by inject()

    private val tokenMapper: TokenToTokenDtoMapper by inject()

    override fun authenticate(route: Route?, response: Response): Request? {
        val retryCount = response.retryCount

        if (response.request.url.pathSegments.last() == CHANGE_PASSWORD_LAST_SEGMENT ||
            retryCount > MAX_RETRY_COUNT
        ) {
            return null
        }

        token?.let { refreshToken(it.refreshToken) }

        val builder = response.request.newBuilder()
        return token?.let {
            val tokenDto = tokenMapper.map(it)
            builder.signWithToken(tokenDto).build()
        }
    }

    private fun refreshToken(refreshToken: String) {
        return runBlocking {
            val result = refreshTokenUseCase(refreshToken)
            result.onSuccess {
                token = it
            }
            result.onError {
                if (it.errorCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    throw RefreshTokenExpiredException()
                }
            }
        }
    }
}

private val Response.retryCount: Int
    get() {
        var currentResponse = priorResponse
        var result = 0
        while (currentResponse != null) {
            result++
            currentResponse = currentResponse.priorResponse
        }
        return result
    }
