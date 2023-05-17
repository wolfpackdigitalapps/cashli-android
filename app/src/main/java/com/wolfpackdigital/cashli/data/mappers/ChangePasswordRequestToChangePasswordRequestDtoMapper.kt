package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.ChangePasswordRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.ChangePasswordRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class ChangePasswordRequestToChangePasswordRequestDtoMapper :
    Mapper<ChangePasswordRequest, ChangePasswordRequestDto> {

    override fun map(input: ChangePasswordRequest) = ChangePasswordRequestDto(
        currentPassword = input.password,
        newPassword = input.newPassword,
        newPasswordConfirmation = input.newPasswordConfirmation
    )
}
