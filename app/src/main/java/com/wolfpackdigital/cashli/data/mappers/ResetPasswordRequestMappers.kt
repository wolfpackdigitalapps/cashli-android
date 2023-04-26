package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.ResetPasswordRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.ResetPasswordRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class ResetPasswordRequestToResetPasswordRequestDtoMapper :
    Mapper<ResetPasswordRequest, ResetPasswordRequestDto> {
    override fun map(input: ResetPasswordRequest): ResetPasswordRequestDto {
        return ResetPasswordRequestDto(
            token = input.token,
            password = input.password,
            passwordConfirmation = input.confirmPassword
        )
    }
}

class ResetPasswordRequestDtoToResetPasswordRequestMapper :
    Mapper<ResetPasswordRequestDto, ResetPasswordRequest> {
    override fun map(input: ResetPasswordRequestDto): ResetPasswordRequest {
        return ResetPasswordRequest(
            token = input.token,
            password = input.password,
            confirmPassword = input.passwordConfirmation
        )
    }

}