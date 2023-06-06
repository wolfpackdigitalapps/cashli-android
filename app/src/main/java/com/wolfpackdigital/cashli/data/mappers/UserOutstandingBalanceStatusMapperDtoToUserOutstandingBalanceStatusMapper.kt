package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.UserOutstandingBalanceStatusDto
import com.wolfpackdigital.cashli.domain.entities.response.UserOutstandingBalanceStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserOutstandingBalanceStatusMapperDtoToUserOutstandingBalanceStatusMapper :
    Mapper<UserOutstandingBalanceStatusDto, UserOutstandingBalanceStatus> {
    override fun map(input: UserOutstandingBalanceStatusDto): UserOutstandingBalanceStatus {
        return UserOutstandingBalanceStatus(
            outstandingBalance = input.outstandingBalance,
            cashAdvanceBalanceDue = input.cashAdvanceBalanceDue,
            repaymentDate = input.repaymentDate
        )
    }
}
