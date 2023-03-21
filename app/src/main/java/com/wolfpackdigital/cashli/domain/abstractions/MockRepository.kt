package com.wolfpackdigital.cashli.domain.abstractions

import com.wolfpackdigital.cashli.domain.entities.MockItem

interface MockRepository {
    suspend fun getMockList(): List<MockItem>
}
