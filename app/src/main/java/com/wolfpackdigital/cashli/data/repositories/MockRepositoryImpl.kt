package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.cashli.data.remote.api.MockAPI
import com.wolfpackdigital.cashli.domain.abstractions.MockRepository
import com.wolfpackdigital.cashli.domain.entities.MockItem

class MockRepositoryImpl(
    private val api: MockAPI,
    private val mockItemMapper: MockItemDtoToMockItemMapper,
) : MockRepository {

    override suspend fun getMockList(): List<MockItem> {
        val mockItemDtos = api.getList()
        return mockItemDtos.map { mockItemMapper.map(it) }
    }
}
