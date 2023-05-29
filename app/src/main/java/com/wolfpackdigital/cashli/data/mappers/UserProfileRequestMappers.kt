package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.UserProfileRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.UserProfileRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserProfileRequestDtoToUserProfileRequestMapper(
    private val languagesMapper: LanguageDtoToLanguageMapper
) : Mapper<UserProfileRequestDto, UserProfileRequest> {
    override fun map(input: UserProfileRequestDto): UserProfileRequest {
        return UserProfileRequest(
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            password = input.password,
            phoneNumber = input.phoneNumber,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language)
        )
    }
}

class UserProfileRequestToUserProfileRequestDtoMapper(
    private val languagesMapper: LanguageToLanguageDtoMapper
) : Mapper<UserProfileRequest, UserProfileRequestDto> {
    override fun map(input: UserProfileRequest): UserProfileRequestDto {
        return UserProfileRequestDto(
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            password = input.password,
            phoneNumber = input.phoneNumber,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language)
        )
    }
}
