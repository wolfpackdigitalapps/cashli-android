package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.response.TransferFeesDto
import retrofit2.http.GET

interface CashAdvanceApi {

    @GET("v1/transfer_fees")
    suspend fun getTransferFees(): List<TransferFeesDto>
}
