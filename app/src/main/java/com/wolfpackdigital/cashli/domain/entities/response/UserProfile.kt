package com.wolfpackdigital.cashli.domain.entities.response

import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.enums.Languages

data class UserProfile(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phoneNumber: String,
    val street: String,
    val zipCode: String,
    val city: String,
    val state: String,
    val language: Languages = Languages.ENGLISH,
    val tokens: Token?,
    val connectionExpired: Boolean,
    val bankAccountConnected: Boolean,
    val lastMembership: LastMembership?,
    val eligibilityStatus: EligibilityStatus,
    val bankAccount: BankAccount?,
    val userSettings: List<UserSetting> = listOf(),
    val accountStatus: AccountStatus
)
