package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.ResetPasswordRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.ResetPasswordRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class ResetPasswordRequestToResetPasswordRequestDtoMapper(
    private val languageMapper: LanguageToLanguageDtoMapper
) : Mapper<ResetPasswordRequest, ResetPasswordRequestDto> {
    override fun map(input: ResetPasswordRequest): ResetPasswordRequestDto {
        return ResetPasswordRequestDto(
            token = input.token,
            password = input.password,
            passwordConfirmation = input.confirmPassword,
            locale = languageMapper.map(input.locale)
        )
    }
}

class ResetPasswordRequestDtoToResetPasswordRequestMapper(
    private val languageMapper: LanguageDtoToLanguageMapper
) : Mapper<ResetPasswordRequestDto, ResetPasswordRequest> {
    override fun map(input: ResetPasswordRequestDto): ResetPasswordRequest {
        return ResetPasswordRequest(
            token = input.token,
            password = input.password,
            confirmPassword = input.passwordConfirmation,
            locale = languageMapper.map(input.locale)
        )
    }
}
