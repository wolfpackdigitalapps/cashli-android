package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.CashAdvanceStatusDto
import com.wolfpackdigital.cashli.domain.entities.enums.CashAdvanceStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class CashAdvanceStatusDtoToCashAdvanceStatusMapper :
    Mapper<CashAdvanceStatusDto, CashAdvanceStatus> {
    override fun map(input: CashAdvanceStatusDto): CashAdvanceStatus {
        return when (input) {
            CashAdvanceStatusDto.PAID -> CashAdvanceStatus.PAID
            CashAdvanceStatusDto.OVERDUE -> CashAdvanceStatus.OVERDUE
            CashAdvanceStatusDto.SCHEDULED -> CashAdvanceStatus.SCHEDULED
        }
    }
}
