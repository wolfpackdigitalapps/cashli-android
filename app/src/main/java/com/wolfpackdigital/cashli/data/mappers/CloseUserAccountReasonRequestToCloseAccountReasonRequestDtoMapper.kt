package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.requests.CloseUserAccountReasonRequestDto
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.shared.base.Mapper

class CloseUserAccountReasonRequestToCloseAccountReasonRequestDtoMapper :
    Mapper<CloseUserAccountReasonRequest, CloseUserAccountReasonRequestDto> {
    override fun map(input: CloseUserAccountReasonRequest): CloseUserAccountReasonRequestDto {
        return CloseUserAccountReasonRequestDto(
            reason = input.reason
        )
    }
}
