package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateIdentifiersCodeValidationRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class UpdateIdentifiersCodeValidationRequestDtoToUpdateIdentifiersCodeValidationRequestMapper(
    private val updateIdentifierChannelMapper: IdentifierChannelDtoToIdentifierChannelMapper
) :
    Mapper<UpdateIdentifiersCodeValidationRequestDto, UpdateIdentifiersCodeValidationRequest> {
    override fun map(input: UpdateIdentifiersCodeValidationRequestDto): UpdateIdentifiersCodeValidationRequest {
        return UpdateIdentifiersCodeValidationRequest(
            channel = updateIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier,
            code = input.code
        )
    }
}

class UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper(
    private val updateIdentifierChannelMapper: IdentifierChannelToIdentifierChannelDtoMapper
) :
    Mapper<UpdateIdentifiersCodeValidationRequest, UpdateIdentifiersCodeValidationRequestDto> {
    override fun map(input: UpdateIdentifiersCodeValidationRequest): UpdateIdentifiersCodeValidationRequestDto {
        return UpdateIdentifiersCodeValidationRequestDto(
            channel = updateIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier,
            code = input.code
        )
    }
}