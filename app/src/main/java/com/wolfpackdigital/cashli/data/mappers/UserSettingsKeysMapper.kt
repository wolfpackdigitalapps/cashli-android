package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.enums.UserSettingsKeysDto
import com.wolfpackdigital.cashli.domain.entities.enums.UserSettingsKeys
import com.wolfpackdigital.cashli.shared.base.Mapper

class UserSettingsKeyToUserSettingsKeyDtoMapper : Mapper<UserSettingsKeys, UserSettingsKeysDto> {
    override fun map(input: UserSettingsKeys): UserSettingsKeysDto {
        return when (input) {
            UserSettingsKeys.DEVICE_TOKEN -> UserSettingsKeysDto.DEVICE_TOKEN
            UserSettingsKeys.LOW_BALANCE_THRESHOLD -> UserSettingsKeysDto.LOW_BALANCE_THRESHOLD
            UserSettingsKeys.PLAID_ITEM_ID -> UserSettingsKeysDto.PLAID_ITEM_ID
            UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED -> UserSettingsKeysDto.PUSH_NOTIFICATIONS_ENABLED
        }
    }
}

class UserSettingsKeyDtoToUserSettingsKeyMapper : Mapper<UserSettingsKeysDto, UserSettingsKeys> {
    override fun map(input: UserSettingsKeysDto): UserSettingsKeys {
        return when (input) {
            UserSettingsKeysDto.DEVICE_TOKEN -> UserSettingsKeys.DEVICE_TOKEN
            UserSettingsKeysDto.LOW_BALANCE_THRESHOLD -> UserSettingsKeys.LOW_BALANCE_THRESHOLD
            UserSettingsKeysDto.PLAID_ITEM_ID -> UserSettingsKeys.PLAID_ITEM_ID
            UserSettingsKeysDto.PUSH_NOTIFICATIONS_ENABLED -> UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED
        }
    }
}
