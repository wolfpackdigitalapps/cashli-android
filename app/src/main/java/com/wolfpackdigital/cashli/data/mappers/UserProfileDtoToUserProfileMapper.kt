package com.wolfpackdigital.cashli.data.mappers

import com.wolfpackdigital.cashli.data.remote.dto.response.UserProfileDto
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.shared.base.Mapper

@Suppress("LongParameterList")
class UserProfileDtoToUserProfileMapper(
    private val languagesMapper: LanguageDtoToLanguageMapper,
    private val tokenMapper: TokenDtoToTokenMapper,
    private val bankAccountMapper: BankAccountDtoToBankAccountMapper,
    private val userSettingMapper: UserSettingDtoToUserSettingMapper,
    private val eligibilityStatusMapper: EligibilityStatusDtoToEligibilityStatusMapper,
    private val accountStatusMapper: AccountStatusDtoToAccountStatusMapper,
    private val lastMembershipMapper: LastMembershipDtoToLastMembershipMapper
) : Mapper<UserProfileDto, UserProfile> {
    override fun map(input: UserProfileDto): UserProfile {
        return UserProfile(
            id = input.id,
            firstName = input.firstName,
            lastName = input.lastName,
            email = input.email,
            phoneNumber = input.phoneNumber,
            street = input.street,
            zipCode = input.zipCode,
            city = input.city,
            state = input.state,
            language = languagesMapper.map(input.language),
            tokens = input.tokens?.let { tokenMapper.map(it) },
            connectionExpired = input.connectionExpired,
            bankAccountConnected = input.bankAccountConnected,
            lastMembership = input.lastMembership?.let { lastMembershipMapper.map(it) },
            eligibilityStatus = eligibilityStatusMapper.map(input.eligibilityStatus),
            bankAccount = input.bankAccount?.let { bankAccountMapper.map(it) },
            userSettings = input.userSettings?.map { userSettingMapper.map(it) } ?: listOf(),
            accountStatus = accountStatusMapper.map(input.accountStatus)
        )
    }
}
