package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.IdentifiersTokenRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersTokenRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class IdentifiersTokenRequestDtoToIdentifiersTokenRequestMapper :
    Mapper<IdentifiersTokenRequestDto, IdentifiersTokenRequest> {
    override fun map(input: IdentifiersTokenRequestDto): IdentifiersTokenRequest {
        return IdentifiersTokenRequest(
            emailToken = input.emailToken,
            phoneNumberToken = input.phoneNumberToken
        )
    }
}

class IdentifiersTokenRequestToIdentifiersTokenRequestDtoMapper :
    Mapper<IdentifiersTokenRequest, IdentifiersTokenRequestDto> {
    override fun map(input: IdentifiersTokenRequest): IdentifiersTokenRequestDto {
        return IdentifiersTokenRequestDto(
            emailToken = input.emailToken,
            phoneNumberToken = input.phoneNumberToken
        )
    }
}
