package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.UserSettingDto
import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserSettingToUserSettingDtoMapper(
    private val userSettingsKeysMapper: UserSettingsKeyToUserSettingsKeyDtoMapper
) :
    Mapper<UserSetting, UserSettingDto> {
    override fun map(input: UserSetting): UserSettingDto {
        return UserSettingDto(
            key = userSettingsKeysMapper.map(input.key),
            value = input.value
        )
    }
}

class UserSettingDtoToUserSettingMapper(
    private val userSettingsKeysMapper: UserSettingsKeyDtoToUserSettingsKeyMapper
) :
    Mapper<UserSettingDto, UserSetting> {
    override fun map(input: UserSettingDto): UserSetting {
        return UserSetting(
            key = userSettingsKeysMapper.map(input.key),
            value = input.value
        )
    }
}
