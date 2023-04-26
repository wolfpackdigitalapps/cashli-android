package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserProfileDtoToUserProfileMapper(
    private val languagesMapper: LanguagesDtoToLanguagesMapper,
    private val tokenMapper: TokenDtoToTokenMapper
) : Mapper<UserProfileDto, UserProfile> {
    override fun map(input: UserProfileDto): UserProfile {
        return UserProfile(
            id = input.id,
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            phoneNumber = input.phoneNumber,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language),
            tokens = tokenMapper.map(input.tokens)
        )
    }
}

class UserProfileToUserProfileDtoMapper(
    private val languagesMapper: LanguagesToLanguagesDtoMapper,
    private val tokenMapper: TokenToTokenDtoMapper
) : Mapper<UserProfile, UserProfileDto> {
    override fun map(input: UserProfile): UserProfileDto {
        return UserProfileDto(
            id = input.id,
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            phoneNumber = input.phoneNumber,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language),
            tokens = tokenMapper.map(input.tokens)
        )
    }
}
