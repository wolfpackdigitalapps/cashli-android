package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.LastMembershipStatusDto
import com.wolfpackdigital.cashli.domain.entities.response.LastMembershipStatus
import com.wolfpackdigital.cashli.shared.base.Mapper

class LastMembershipStatusDtoToLastMembershipStatusMapper : Mapper<LastMembershipStatusDto, LastMembershipStatus> {
    override fun map(input: LastMembershipStatusDto): LastMembershipStatus {
        return when (input) {
            LastMembershipStatusDto.PAYMENT_PENDING -> LastMembershipStatus.PAYMENT_PENDING
            LastMembershipStatusDto.ACTIVE -> LastMembershipStatus.ACTIVE
            LastMembershipStatusDto.EXPIRED -> LastMembershipStatus.EXPIRED
        }
    }
}
