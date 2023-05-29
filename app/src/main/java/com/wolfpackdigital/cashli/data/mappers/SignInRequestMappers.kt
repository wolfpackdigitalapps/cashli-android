package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.SignInRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class SignInRequestDtoToSignInRequestMapper(
    private val userSignInRequestMapper: UserSignInRequestDtoToUserSignInRequestMapper,
    private val languageMapper: LanguageDtoToLanguageMapper
) : Mapper<SignInRequestDto, SignInRequest> {
    override fun map(input: SignInRequestDto): SignInRequest {
        return SignInRequest(
            userSignInRequest = userSignInRequestMapper.map(input.userSignInRequest),
            locale = languageMapper.map(input.locale)
        )
    }
}

class SignInRequestToSignInRequestDtoMapper(
    private val userSignInRequestMapper: UserSignInRequestToUserSignInRequestDtoMapper,
    private val languageMapper: LanguageToLanguageDtoMapper
) : Mapper<SignInRequest, SignInRequestDto> {
    override fun map(input: SignInRequest): SignInRequestDto {
        return SignInRequestDto(
            userSignInRequest = userSignInRequestMapper.map(input.userSignInRequest),
            locale = languageMapper.map(input.locale)
        )
    }
}
