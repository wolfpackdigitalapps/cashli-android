package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.requests.CashAdvanceRequestDto
import com.wolfpackdigital.cashli.data.remote.dto.response.TransferFeesDto
import retrofit2.http.GET
import retrofit2.http.POST

interface CashAdvanceApi {

    @GET("v1/transfer_fees")
    suspend fun getTransferFees(): List<TransferFeesDto>

    @POST("v1/cash_advances/requests")
    suspend fun requestCashAdvance(request: CashAdvanceRequestDto)
}
