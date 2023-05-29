package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.UpdateUserProfileRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class UpdateUserProfileRequestDtoToUserProfileRequestMapper(
    private val languagesMapper: LanguageDtoToLanguageMapper
) : Mapper<UpdateUserProfileRequestDto, UpdateUserProfileRequest> {
    override fun map(input: UpdateUserProfileRequestDto): UpdateUserProfileRequest {
        return UpdateUserProfileRequest(
            firstName = input.firstName,
            lastName = input.lastName,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language)
        )
    }
}

class UpdateUserProfileRequestToUserProfileRequestDtoMapper(
    private val languagesMapper: LanguageToLanguageDtoMapper
) : Mapper<UpdateUserProfileRequest, UpdateUserProfileRequestDto> {
    override fun map(input: UpdateUserProfileRequest): UpdateUserProfileRequestDto {
        return UpdateUserProfileRequestDto(
            firstName = input.firstName,
            lastName = input.lastName,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language)
        )
    }
}
