package com.wolfpackdigital.cashli.data.remote.api.common

import com.wolfpackdigital.cashli.data.remote.dto.response.TokenDto
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private const val AUTHORIZATION = "Authorization"
private const val BEARER = "Bearer"

class AuthenticationInterceptor : Interceptor, PersistenceService {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        token?.let {
            requestBuilder.addHeader(AUTHORIZATION, "$BEARER ${it.accessToken}")
        }
        return chain.proceed(requestBuilder.build())
    }
}

fun Request.Builder.signWithToken(token: TokenDto): Request.Builder {
    header(AUTHORIZATION, "$BEARER ${token.accessToken}")
    return this
}
