package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.IdentifierTokenDto
import com.wolfpackdigital.cashli.domain.entities.response.IdentifierToken
import com.wolfpackdigital.cashli.shared.base.Mapper

class IdentifierTokenDtoToIdentifierTokenMapper :
    Mapper<IdentifierTokenDto, IdentifierToken> {
    override fun map(input: IdentifierTokenDto): IdentifierToken {
        return IdentifierToken(
            token = input.token
        )
    }
}

class IdentifierTokenToIdentifierTokenDtoMapper :
    Mapper<IdentifierToken, IdentifierTokenDto> {
    override fun map(input: IdentifierToken): IdentifierTokenDto {
        return IdentifierTokenDto(
            token = input.token
        )
    }
}
