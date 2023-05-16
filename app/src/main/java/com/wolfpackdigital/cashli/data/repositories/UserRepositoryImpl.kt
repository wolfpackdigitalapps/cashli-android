package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.BankTransactionDtoToBankTransactionMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingDtoToUserSettingMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingToUserSettingDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.BankTransactionsRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting

class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
    private val userSettingToDtoMapper: UserSettingToUserSettingDtoMapper,
    private val userSettingMapper: UserSettingDtoToUserSettingMapper,
    private val bankTransactionMapper: BankTransactionDtoToBankTransactionMapper
) : UserRepository {
    override suspend fun getUserProfile(): UserProfile {
        val result = userApi.getUserProfile()
        return userProfileMapper.map(result)
    }

    override suspend fun getUserBankTransactions(
        bankTransactionsRequest: BankTransactionsRequest
    ): List<BankTransaction> {
        val result = userApi.getUserBankTransaction(
            bankTransactionsRequest.page,
            bankTransactionsRequest.perPage
        )
        return result.map { bankTransactionMapper.map(it) }
    }

    override suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting {
        val result = userApi.updateUserProfileSetting(userSettingToDtoMapper.map(userSetting))
        return userSettingMapper.map(result)
    }
}
