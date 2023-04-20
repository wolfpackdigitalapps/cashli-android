package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.RegistrationIdentifierChannelDto
import com.wolfpackdigital.cashli.domain.entities.enums.RegistrationIdentifierChannel
import com.wolfpackdigital.cashli.shared.base.Mapper

class RegistrationIdentifierChannelDtoToRegistrationIdentifierChannelMapper :
    Mapper<RegistrationIdentifierChannelDto, RegistrationIdentifierChannel> {
    override fun map(input: RegistrationIdentifierChannelDto): RegistrationIdentifierChannel {
        return when (input) {
            RegistrationIdentifierChannelDto.SMS -> RegistrationIdentifierChannel.SMS
            RegistrationIdentifierChannelDto.EMAIL -> RegistrationIdentifierChannel.EMAIL
        }
    }
}

class RegistrationIdentifierChannelToRegistrationIdentifierChannelDtoMapper :
    Mapper<RegistrationIdentifierChannel, RegistrationIdentifierChannelDto> {
    override fun map(input: RegistrationIdentifierChannel): RegistrationIdentifierChannelDto {
        return when (input) {
            RegistrationIdentifierChannel.SMS -> RegistrationIdentifierChannelDto.SMS
            RegistrationIdentifierChannel.EMAIL -> RegistrationIdentifierChannelDto.EMAIL
        }
    }
}
