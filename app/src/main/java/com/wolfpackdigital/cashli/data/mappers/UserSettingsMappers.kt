package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.UserSettingDto
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserSettingToUserSettingDtoMapper :
    Mapper<UserSetting, UserSettingDto> {
    override fun map(input: UserSetting): UserSettingDto {
        return UserSettingDto(
            key = input.key,
            value = input.value
        )
    }
}

class UserSettingDtoToUserSettingMapper :
    Mapper<UserSettingDto, UserSetting> {
    override fun map(input: UserSettingDto): UserSetting {
        return UserSetting(
            key = input.key,
            value = input.value
        )
    }
}
