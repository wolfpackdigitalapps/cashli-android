package com.wolfpackdigital.baseproject.domain.abstractions

import com.wolfpackdigital.baseproject.domain.entities.MockItem

interface MockRepository {
    suspend fun getMockList(): List<MockItem>
}