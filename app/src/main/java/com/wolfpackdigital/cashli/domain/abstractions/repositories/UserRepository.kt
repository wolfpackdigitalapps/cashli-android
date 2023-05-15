package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.requests.BankTransactionsRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting

interface UserRepository {
    suspend fun getUserProfile(): UserProfile
    suspend fun getUserBankTransactions(bankTransactionsRequest: BankTransactionsRequest): List<BankTransaction>
    suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting
}
