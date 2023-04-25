package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.PasswordIdentifiersRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.PasswordIdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class PasswordIdentifiersRequestDtoToPasswordIdentifiersRequestMapper(
    private val registrationIdentifierChannelMapper: RegistrationIdentifierChannelDtoToRegistrationIdentifierChannelMapper
) : Mapper<PasswordIdentifiersRequestDto, PasswordIdentifiersRequest> {
    override fun map(input: PasswordIdentifiersRequestDto): PasswordIdentifiersRequest {
        return PasswordIdentifiersRequest(
            channel = registrationIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier
        )
    }
}

class PasswordIdentifiersRequestToPasswordIdentifiersRequestDtoMapper(
    private val registrationIdentifiersChannelMapper: RegistrationIdentifierChannelToRegistrationIdentifierChannelDtoMapper
) : Mapper<PasswordIdentifiersRequest, PasswordIdentifiersRequestDto> {
    override fun map(input: PasswordIdentifiersRequest): PasswordIdentifiersRequestDto {
        return PasswordIdentifiersRequestDto(
            channel = registrationIdentifiersChannelMapper.map(input.channel),
            identifier = input.identifier
        )
    }
}