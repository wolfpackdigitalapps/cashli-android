package com.wolfpackdigital.cashli.data.remote.api.common

import okhttp3.Interceptor
import okhttp3.Response

// private const val AUTHORIZATION = "Authorization"
// private const val BEARER = "Bearer"

class AuthenticationInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
//        token?.let {
//            requestBuilder.addHeader(AUTHORIZATION, "$BEARER ${it.accessToken}")
//        }
        return chain.proceed(requestBuilder.build())
    }
}
