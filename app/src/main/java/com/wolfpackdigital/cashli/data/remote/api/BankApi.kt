package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.CompleteLinkBankAccountRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.BankTokenDto
import com.wolfpackdigital.cashli.data.remote.dto.response.EligibilityStatusDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface BankApi {

    @GET("v1/eligibility_checks/statuses")
    suspend fun getEligibilityStatus(): EligibilityStatusDto

    @POST("v1/bank_accounts/link")
    suspend fun generateLinkToken(): BankTokenDto

    @PATCH("v1/bank_accounts/link")
    suspend fun completeLinkingBankAccount(
        @Body linkBankAccountDtoRequest: CompleteLinkBankAccountRequestDto
    )
}
