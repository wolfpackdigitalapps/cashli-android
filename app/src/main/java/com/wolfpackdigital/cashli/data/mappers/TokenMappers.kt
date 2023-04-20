package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.TokenDto
import com.wolfpackdigital.cashli.domain.entities.response.Token
import com.wolfpackdigital.cashli.shared.base.Mapper

class TokenDtoToTokenMapper : Mapper<TokenDto, Token> {
    override fun map(input: TokenDto): Token {
        return Token(
            input.accessToken,
            input.tokenType,
            input.expiresIn,
            input.refreshToken,
            input.createdAt
        )
    }
}

class TokenToTokenDtoMapper : Mapper<Token, TokenDto> {
    override fun map(input: Token): TokenDto {
        return TokenDto(
            input.accessToken,
            input.tokenType,
            input.expiresIn,
            input.refreshToken,
            input.createdAt
        )
    }
}
