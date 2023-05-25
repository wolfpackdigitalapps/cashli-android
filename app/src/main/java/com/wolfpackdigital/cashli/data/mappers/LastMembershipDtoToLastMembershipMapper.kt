package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.LastMembershipDto
import com.wolfpackdigital.cashli.domain.entities.response.LastMembership
import com.wolfpackdigital.cashli.shared.base.Mapper

class LastMembershipDtoToLastMembershipMapper(
    private val lastMembershipStatusMapper: LastMembershipStatusDtoToLastMembershipStatusMapper
) : Mapper<LastMembershipDto, LastMembership> {
    override fun map(input: LastMembershipDto) = LastMembership(
        id = input.id,
        startDate = input.startDate,
        endDate = input.endDate,
        status = lastMembershipStatusMapper.map(input.status),
        amount = input.amount,
        isMembershipActive = input.membershipStatus.isActive
    )
}
