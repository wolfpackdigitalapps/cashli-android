package com.wolfpackdigital.baseproject.data.repositories

import com.wolfpackdigital.baseproject.data.mappers.MockItemDtoToMockItemMapper
import com.wolfpackdigital.baseproject.data.remote.api.MockAPI
import com.wolfpackdigital.baseproject.domain.abstractions.MockRepository
import com.wolfpackdigital.baseproject.domain.entities.MockItem

class MockRepositoryImpl(
    private val api: MockAPI,
    private val mockItemMapper: MockItemDtoToMockItemMapper,
    ) : MockRepository {

    override suspend fun getMockList(): List<MockItem> {
        val mockItemDtos = api.getList()
        return mockItemDtos.map { mockItemMapper.map(it) }
    }

}
