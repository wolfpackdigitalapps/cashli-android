package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.EligibilityStatusDto
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class EligibilityStatusDtoToEligibilityStatusMapper :
    Mapper<EligibilityStatusDto, EligibilityStatus> {
    override fun map(input: EligibilityStatusDto): EligibilityStatus {
        return EligibilityStatus(
            eligible = input.eligible
        )
    }
}

class EligibilityStatusToEligibilityStatusDtoMapper :
    Mapper<EligibilityStatus, EligibilityStatusDto> {
    override fun map(input: EligibilityStatus): EligibilityStatusDto {
        return EligibilityStatusDto(
            eligible = input.eligible
        )
    }
}
