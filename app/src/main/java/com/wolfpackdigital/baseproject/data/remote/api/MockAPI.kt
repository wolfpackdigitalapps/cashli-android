package com.wolfpackdigital.baseproject.data.remote.api

import com.wolfpackdigital.baseproject.data.remote.dto.MockItemDto
import retrofit2.http.GET

interface MockAPI {
    @GET("items")
    suspend fun getList(): List<MockItemDto>
}
