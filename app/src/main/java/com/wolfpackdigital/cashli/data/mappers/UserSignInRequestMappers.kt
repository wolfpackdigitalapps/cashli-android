package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.UserSignInRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.UserSignInRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserSignInRequestDtoToUserSignInRequestMapper :
    Mapper<UserSignInRequestDto, UserSignInRequest> {
    override fun map(input: UserSignInRequestDto): UserSignInRequest {
        return UserSignInRequest(
            identifier = input.identifier,
            password = input.password
        )
    }
}

class UserSignInRequestToUserSignInRequestDtoMapper :
    Mapper<UserSignInRequest, UserSignInRequestDto> {
    override fun map(input: UserSignInRequest): UserSignInRequestDto {
        return UserSignInRequestDto(
            identifier = input.identifier,
            password = input.password
        )
    }
}