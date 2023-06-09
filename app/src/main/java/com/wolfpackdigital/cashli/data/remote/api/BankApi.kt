package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.CompleteLinkBankAccountRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.BankTokenDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST

interface BankApi {

    @POST("v1/bank_accounts/link")
    suspend fun generateLinkToken(): BankTokenDto

    @PATCH("v1/bank_accounts/link")
    suspend fun completeLinkingBankAccount(
        @Body linkBankAccountDtoRequest: CompleteLinkBankAccountRequestDto
    )

    @DELETE("v1/bank_accounts/link")
    suspend fun unlinkAccount()

    @POST("v1/bank_accounts/update_link")
    suspend fun generateUpdateLinkToken(): BankTokenDto

    @PATCH("v1/bank_accounts/update_link")
    suspend fun completeUpdateLinkingBankAccount(
        @Body updateLinkBankAccountRequest: CompleteLinkBankAccountRequestDto
    )
}
