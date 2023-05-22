package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.AccountStatusDto
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class AccountStatusDtoToAccountStatusMapper : Mapper<AccountStatusDto, AccountStatus> {
    override fun map(input: AccountStatusDto): AccountStatus {
        return when (input) {
            AccountStatusDto.ACTIVE -> AccountStatus.ACTIVE
            AccountStatusDto.LOCKED -> AccountStatus.LOCKED
            AccountStatusDto.PAUSED -> AccountStatus.PAUSED
            AccountStatusDto.CLOSED -> AccountStatus.CLOSED
            AccountStatusDto.SUSPENDED -> AccountStatus.SUSPENDED
        }
    }
}
