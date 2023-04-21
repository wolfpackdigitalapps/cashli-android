package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class IdentifiersCodeValidationRequestDtoToIdentifiersCodeValidationRequestMapper :
    Mapper<IdentifiersCodeValidationRequestDto, IdentifiersCodeValidationRequest> {
    override fun map(input: IdentifiersCodeValidationRequestDto): IdentifiersCodeValidationRequest {
        return IdentifiersCodeValidationRequest(
            identifier = input.identifier,
            code = input.code
        )
    }
}

class IdentifiersCodeValidationRequestToIdentifiersCodeValidationRequestDtoMapper :
    Mapper<IdentifiersCodeValidationRequest, IdentifiersCodeValidationRequestDto> {
    override fun map(input: IdentifiersCodeValidationRequest): IdentifiersCodeValidationRequestDto {
        return IdentifiersCodeValidationRequestDto(
            identifier = input.identifier,
            code = input.code
        )
    }
}
