package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.EligibilityChecksDto
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks
import com.wolfpackdigital.cashli.shared.base.Mapper

class EligibilityChecksDtoToEligibilityChecksMapper(
    private val eligibilityStatusMapper: EligibilityStatusDtoToEligibilityStatusMapper
) : Mapper<EligibilityChecksDto, EligibilityChecks> {
    override fun map(input: EligibilityChecksDto): EligibilityChecks {
        return EligibilityChecks(
            status = eligibilityStatusMapper.map(input.status),
        )
    }
}

class EligibilityChecksToEligibilityChecksDtoMapper(
    private val eligibilityStatusMapper: EligibilityStatusToEligibilityStatusDtoMapper
) : Mapper<EligibilityChecks, EligibilityChecksDto> {
    override fun map(input: EligibilityChecks): EligibilityChecksDto {
        return EligibilityChecksDto(
            status = eligibilityStatusMapper.map(input.status),
        )
    }
}