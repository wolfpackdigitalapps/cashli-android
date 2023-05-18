package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.EligibilityStatusDto
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class EligibilityStatusDtoToEligibilityStatusMapper :
    Mapper<EligibilityStatusDto, EligibilityStatus> {
    override fun map(input: EligibilityStatusDto): EligibilityStatus {
        return when (input) {
            EligibilityStatusDto.BANK_ACCOUNT_NOT_CONNECTED -> EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED
            EligibilityStatusDto.ELIGIBILITY_CHECK_PENDING -> EligibilityStatus.ELIGIBILITY_CHECK_PENDING
            EligibilityStatusDto.ELIGIBLE -> EligibilityStatus.ELIGIBLE
            EligibilityStatusDto.NOT_ELIGIBLE -> EligibilityStatus.NOT_ELIGIBLE
        }
    }
}

class EligibilityStatusToEligibilityStatusDtoMapper :
    Mapper<EligibilityStatus, EligibilityStatusDto> {
    override fun map(input: EligibilityStatus): EligibilityStatusDto {
        return when (input) {
            EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED -> EligibilityStatusDto.BANK_ACCOUNT_NOT_CONNECTED
            EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> EligibilityStatusDto.ELIGIBILITY_CHECK_PENDING
            EligibilityStatus.ELIGIBLE -> EligibilityStatusDto.ELIGIBLE
            EligibilityStatus.NOT_ELIGIBLE -> EligibilityStatusDto.NOT_ELIGIBLE
        }
    }
}
