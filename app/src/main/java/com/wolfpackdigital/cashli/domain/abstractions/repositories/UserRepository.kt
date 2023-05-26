package com.wolfpackdigital.cashli.domain.abstractions.repositories

import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.domain.entities.requests.BankTransactionsRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

interface UserRepository {
    suspend fun pauseUserAccount()
    suspend fun getUserProfile(): UserProfile
    suspend fun getUserBankTransactions(bankTransactionsRequest: BankTransactionsRequest): List<BankTransaction>
    suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting
    suspend fun submitChangeIdentifiers(
        changeIdentifiersRequest: IdentifiersRequest
    )

    suspend fun updateChangeIdentifiers(
        updateIdentifiersRequest: UpdateIdentifiersCodeValidationRequest
    ): UserProfile

    suspend fun updateUserProfile(
        updateUserProfileRequest: UpdateUserProfileRequest
    ): UserProfile
}
