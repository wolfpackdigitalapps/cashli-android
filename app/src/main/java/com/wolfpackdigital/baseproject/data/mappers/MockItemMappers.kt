package com.wolfpackdigital.baseproject.data.mappers

import com.wolfpackdigital.baseproject.data.remote.dto.MockItemDto
import com.wolfpackdigital.baseproject.domain.entities.MockItem
import com.wolfpackdigital.baseproject.shared.base.Mapper

class MockItemDtoToMockItemMapper: Mapper<MockItemDto, MockItem> {
    override fun map(input: MockItemDto): MockItem {
        return MockItem(input.id, input.content, input.priority)
    }
}

class MockItemToMockItemDtoMapper: Mapper<MockItem, MockItemDto> {
    override fun map(input: MockItem): MockItemDto {
        return MockItemDto(input.id, input.content, input.priority)
    }
}
