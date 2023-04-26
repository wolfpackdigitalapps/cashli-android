package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.IdentifierChannelDto
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.shared.base.Mapper

class IdentifierChannelDtoToIdentifierChannelMapper :
    Mapper<IdentifierChannelDto, IdentifierChannel> {
    override fun map(input: IdentifierChannelDto): IdentifierChannel {
        return when (input) {
            IdentifierChannelDto.SMS -> IdentifierChannel.SMS
            IdentifierChannelDto.EMAIL -> IdentifierChannel.EMAIL
        }
    }
}

class IdentifierChannelToIdentifierChannelDtoMapper :
    Mapper<IdentifierChannel, IdentifierChannelDto> {
    override fun map(input: IdentifierChannel): IdentifierChannelDto {
        return when (input) {
            IdentifierChannel.SMS -> IdentifierChannelDto.SMS
            IdentifierChannel.EMAIL -> IdentifierChannelDto.EMAIL
        }
    }
}
