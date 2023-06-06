package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.CashAdvanceRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.CashAdvanceDetailsDto
import com.wolfpackdigital.cashli.data.remote.dto.response.EligibilityChecksDto
import com.wolfpackdigital.cashli.data.remote.dto.response.TransferFeesDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface CashAdvanceApi {

    @GET("v1/cash_advances/limits")
    suspend fun getCashAdvancesLimits(): EligibilityChecksDto

    @GET("v1/transfer_fees")
    suspend fun getTransferFees(): List<TransferFeesDto>

    @POST("v1/cash_advances/requests")
    suspend fun requestCashAdvance(
        @Body request: CashAdvanceRequestDto
    ): CashAdvanceDetailsDto

    @GET("v1/cash_advances/history")
    suspend fun getCashAdvancesHistory(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): List<CashAdvanceDetailsDto>
}
