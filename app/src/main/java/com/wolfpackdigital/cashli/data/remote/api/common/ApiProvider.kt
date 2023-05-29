package com.wolfpackdigital.cashli.data.remote.api.common

import com.google.gson.GsonBuilder
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.data.remote.api.AuthApi
import com.wolfpackdigital.cashli.data.remote.api.BankApi
import com.wolfpackdigital.cashli.data.remote.api.CashAdvanceApi
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private const val TIMEOUT = 30L

object ApiProvider {

    private val authInterceptor: AuthenticationInterceptor by lazy {
        AuthenticationInterceptor()
    }

    private val refreshInterceptor: RefreshTokenInterceptor by lazy {
        RefreshTokenInterceptor()
    }

    private val gsonConverterFactory: GsonConverterFactory = let {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        GsonConverterFactory.create(gson)
    }

    private val okHttpClient: OkHttpClient = let {
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(refreshInterceptor)
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        if (BuildConfig.DEBUG) {
            client.addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            )
        }
        client.build()
    }

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(gsonConverterFactory)
        .client(okHttpClient)
        .build()

    fun provideAuthApi(): AuthApi = retrofit.create(AuthApi::class.java)
    fun provideBankApi(): BankApi = retrofit.create(BankApi::class.java)
    fun provideUserApi(): UserApi = retrofit.create(UserApi::class.java)
    fun provideCashAdvanceApi(): CashAdvanceApi = retrofit.create(CashAdvanceApi::class.java)
}
