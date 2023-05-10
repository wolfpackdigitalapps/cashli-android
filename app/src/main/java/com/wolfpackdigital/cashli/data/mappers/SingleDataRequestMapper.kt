package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.SingleDataRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.SingleDataRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class SingleDataRequestToSingleDataRequestDtoMapper :
    Mapper<SingleDataRequest, SingleDataRequestDto> {
    override fun map(input: SingleDataRequest): SingleDataRequestDto {
        return SingleDataRequestDto(
            value = input.value

        )
    }
}

class SingleDataRequestDtoToSingleDataRequestMapper :
    Mapper<SingleDataRequestDto, SingleDataRequest> {
    override fun map(input: SingleDataRequestDto): SingleDataRequest {
        return SingleDataRequest(
            value = input.value
        )
    }
}
