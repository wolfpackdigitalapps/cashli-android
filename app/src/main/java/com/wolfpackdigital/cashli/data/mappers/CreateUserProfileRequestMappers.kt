package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.CreateUserProfileRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class CreateUserProfileRequestDtoToCreateUserProfileRequestMapper(
    private val identifiersTokenRequestMapper: IdentifiersTokenRequestDtoToIdentifiersTokenRequestMapper,
    private val userProfileRequestMapper: UserProfileRequestDtoToUserProfileRequestMapper
) : Mapper<CreateUserProfileRequestDto, CreateUserProfileRequest> {
    override fun map(input: CreateUserProfileRequestDto): CreateUserProfileRequest {
        return CreateUserProfileRequest(
            userProfileRequest = userProfileRequestMapper.map(input.userProfileRequest),
            identifiersTokenRequest = identifiersTokenRequestMapper.map(input.identifiersTokenRequest)
        )
    }
}

class CreateUserProfileRequestToCreateUserProfileRequestDtoMapper(
    private val identifiersTokenRequestMapper: IdentifiersTokenRequestToIdentifiersTokenRequestDtoMapper,
    private val userProfileRequestMapper: UserProfileRequestToUserProfileRequestDtoMapper
) : Mapper<CreateUserProfileRequest, CreateUserProfileRequestDto> {
    override fun map(input: CreateUserProfileRequest): CreateUserProfileRequestDto {
        return CreateUserProfileRequestDto(
            userProfileRequest = userProfileRequestMapper.map(input.userProfileRequest),
            identifiersTokenRequest = identifiersTokenRequestMapper.map(input.identifiersTokenRequest)
        )
    }
}
