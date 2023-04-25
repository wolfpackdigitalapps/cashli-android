package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.PasswordIdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.PasswordIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class PasswordIdentifiersCodeValidationRequestDtoToPasswordIdentifiersCodeValidationRequestMapper :
    Mapper<PasswordIdentifiersCodeValidationRequestDto, PasswordIdentifiersCodeValidationRequest> {
    override fun map(input: PasswordIdentifiersCodeValidationRequestDto): PasswordIdentifiersCodeValidationRequest {
        return PasswordIdentifiersCodeValidationRequest(
            identifier = input.identifier,
            code = input.code
        )
    }
}

class PasswordIdentifiersCodeValidationRequestToPasswordIdentifiersCodeValidationRequestDtoMapper :
    Mapper<PasswordIdentifiersCodeValidationRequest, PasswordIdentifiersCodeValidationRequestDto> {
    override fun map(input: PasswordIdentifiersCodeValidationRequest): PasswordIdentifiersCodeValidationRequestDto {
        return PasswordIdentifiersCodeValidationRequestDto(
            identifier = input.identifier,
            code = input.code
        )
    }
}