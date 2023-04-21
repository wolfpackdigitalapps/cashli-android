package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.RegistrationIdentifiersRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.RegistrationIdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

@Suppress("MaxLineLength")
class RegistrationIdentifiersRequestDtoToRegistrationIdentifiersRequestMapper(
    private val registrationIdentifierChannelMapper: RegistrationIdentifierChannelDtoToRegistrationIdentifierChannelMapper
) : Mapper<RegistrationIdentifiersRequestDto, RegistrationIdentifiersRequest> {
    override fun map(input: RegistrationIdentifiersRequestDto): RegistrationIdentifiersRequest {
        return RegistrationIdentifiersRequest(
            channel = registrationIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier
        )
    }
}

@Suppress("MaxLineLength")
class RegistrationIdentifiersRequestToRegistrationIdentifiersRequestDtoMapper(
    private val registrationIdentifierChannelMapper: RegistrationIdentifierChannelToRegistrationIdentifierChannelDtoMapper
) : Mapper<RegistrationIdentifiersRequest, RegistrationIdentifiersRequestDto> {
    override fun map(input: RegistrationIdentifiersRequest): RegistrationIdentifiersRequestDto {
        return RegistrationIdentifiersRequestDto(
            channel = registrationIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier
        )
    }
}
