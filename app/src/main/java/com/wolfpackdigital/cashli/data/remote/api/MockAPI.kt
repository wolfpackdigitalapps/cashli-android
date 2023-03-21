package com.wolfpackdigital.cashli.data.remote.api

import com.wolfpackdigital.cashli.data.remote.dto.MockItemDto
import retrofit2.http.GET

interface MockAPI {
    @GET("items")
    suspend fun getList(): List<MockItemDto>
}
