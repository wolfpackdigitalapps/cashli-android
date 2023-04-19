package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.RefreshTokenRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.RefreshTokenRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class RefreshTokenRequestToRefreshTokenRequestDtoMapper :
    Mapper<RefreshTokenRequest, RefreshTokenRequestDto> {
    override fun map(input: RefreshTokenRequest): RefreshTokenRequestDto {
        return RefreshTokenRequestDto(
            input.grantType,
            input.refreshToken
        )
    }
}

class RefreshTokenRequestDtoToRefreshTokenRequestMapper :
    Mapper<RefreshTokenRequestDto, RefreshTokenRequest> {
    override fun map(input: RefreshTokenRequestDto): RefreshTokenRequest {
        return RefreshTokenRequest(
            input.grantType,
            input.refreshToken
        )
    }
}
