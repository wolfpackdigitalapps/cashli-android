package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.RegistrationIdentifierChannelDto
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.shared.base.Mapper

class IdentifierChannelDtoToIdentifierChannelMapper :
    Mapper<RegistrationIdentifierChannelDto, IdentifierChannel> {
    override fun map(input: RegistrationIdentifierChannelDto): IdentifierChannel {
        return when (input) {
            RegistrationIdentifierChannelDto.SMS -> IdentifierChannel.SMS
            RegistrationIdentifierChannelDto.EMAIL -> IdentifierChannel.EMAIL
        }
    }
}

class IdentifierChannelToIdentifierChannelDtoMapper :
    Mapper<IdentifierChannel, RegistrationIdentifierChannelDto> {
    override fun map(input: IdentifierChannel): RegistrationIdentifierChannelDto {
        return when (input) {
            IdentifierChannel.SMS -> RegistrationIdentifierChannelDto.SMS
            IdentifierChannel.EMAIL -> RegistrationIdentifierChannelDto.EMAIL
        }
    }
}
