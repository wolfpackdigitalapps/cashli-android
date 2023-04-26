package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.BankTokenDto
import com.wolfpackdigital.cashli.domain.entities.response.BankToken
import com.wolfpackdigital.cashli.shared.base.Mapper
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

class BankTokenToBankTokenDtoMapper :
    Mapper<BankToken, BankTokenDto> {
    override fun map(input: BankToken): BankTokenDto {
        return BankTokenDto(
            linkToken = input.linkToken,
            expiration = input.expiration,
            redirectUri = input.redirectUri
        )
    }
}

class BankTokenDtoToBankTokenMapper :
    Mapper<BankTokenDto, BankToken> {
    override fun map(input: BankTokenDto): BankToken {
        return BankToken(
            linkToken = input.linkToken,
            expiration = input.expiration,
            redirectUri = input.redirectUri ?: EMPTY_STRING
        )
    }
}
