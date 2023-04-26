package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.PasswordIdentifierTokenDto
import com.wolfpackdigital.cashli.domain.entities.response.PasswordIdentifierToken
import com.wolfpackdigital.cashli.shared.base.Mapper

class PasswordIdentifierTokenDtoToPasswordIdentifierTokenMapper :
    Mapper<PasswordIdentifierTokenDto, PasswordIdentifierToken> {
    override fun map(input: PasswordIdentifierTokenDto): PasswordIdentifierToken {
        return PasswordIdentifierToken(
            token = input.token
        )
    }
}

class PasswordIdentifierTokenToPasswordIdentifierTokenDtoMapper :
    Mapper<PasswordIdentifierToken, PasswordIdentifierTokenDto> {
    override fun map(input: PasswordIdentifierToken): PasswordIdentifierTokenDto {
        return PasswordIdentifierTokenDto(
            token = input.token
        )
    }
}
