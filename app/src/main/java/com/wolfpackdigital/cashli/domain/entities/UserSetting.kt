package com.wolfpackdigital.cashli.domain.entities

import com.wolfpackdigital.cashli.domain.entities.enums.UserSettingsKeys

data class UserSetting(
    val key: UserSettingsKeys,
    val value: String
)
