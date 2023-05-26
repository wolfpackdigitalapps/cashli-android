package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

@Suppress("MaxLineLength")
class IdentifiersRequestDtoToIdentifiersRequestMapper(
    private val registrationIdentifierChannelMapper: IdentifierChannelDtoToIdentifierChannelMapper,
    private val languageMapper: LanguageDtoToLanguageMapper
) : Mapper<IdentifiersRequestDto, IdentifiersRequest> {
    override fun map(input: IdentifiersRequestDto): IdentifiersRequest {
        return IdentifiersRequest(
            channel = registrationIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier,
            locale = languageMapper.map(input.locale)
        )
    }
}

@Suppress("MaxLineLength")
class IdentifiersRequestToIdentifiersRequestDtoMapper(
    private val registrationIdentifierChannelMapper: IdentifierChannelToIdentifierChannelDtoMapper,
    private val languageMapper: LanguageToLanguageDtoMapper
) : Mapper<IdentifiersRequest, IdentifiersRequestDto> {
    override fun map(input: IdentifiersRequest): IdentifiersRequestDto {
        return IdentifiersRequestDto(
            channel = registrationIdentifierChannelMapper.map(input.channel),
            identifier = input.identifier,
            locale = languageMapper.map(input.locale)
        )
    }
}
