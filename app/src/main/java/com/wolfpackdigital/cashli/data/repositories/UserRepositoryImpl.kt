package com.wolfpackdigital.cashli.data.repositories

import com.wolfpackdigital.cashli.data.mappers.BankTransactionDtoToBankTransactionMapper
import com.wolfpackdigital.cashli.data.mappers.CloseUserAccountReasonRequestToCloseAccountReasonRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.IdentifiersRequestToIdentifiersRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UpdateUserProfileRequestToUserProfileRequestDtoMapper
import com.wolfpackdigital.cashli.data.mappers.UserOutstandingBalanceStatusMapperDtoToUserOutstandingBalanceStatusMapper
import com.wolfpackdigital.cashli.data.mappers.UserProfileDtoToUserProfileMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingDtoToUserSettingMapper
import com.wolfpackdigital.cashli.data.mappers.UserSettingToUserSettingDtoMapper
import com.wolfpackdigital.cashli.data.remote.api.UserApi
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.domain.entities.requests.CloseUserAccountReasonRequest
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.PaginationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateIdentifiersCodeValidationRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.domain.entities.response.UserOutstandingBalanceStatus
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile

@Suppress("LongParameterList", "MaxLineLength")
class UserRepositoryImpl(
    private val userApi: UserApi,
    private val userProfileMapper: UserProfileDtoToUserProfileMapper,
    private val userSettingToDtoMapper: UserSettingToUserSettingDtoMapper,
    private val userSettingMapper: UserSettingDtoToUserSettingMapper,
    private val bankTransactionMapper: BankTransactionDtoToBankTransactionMapper,
    private val changeIdentifiersRequestMapper: IdentifiersRequestToIdentifiersRequestDtoMapper,
    private val updateIdentifiersRequestMapper: UpdateIdentifiersCodeValidationRequestToUpdateIdentifiersCodeValidationRequestDtoMapper,
    private val updateUserProfileRequestMapper: UpdateUserProfileRequestToUserProfileRequestDtoMapper,
    private val closeUserAccountReasonRequestMapper: CloseUserAccountReasonRequestToCloseAccountReasonRequestDtoMapper,
    private val userOutstandingBalanceStatusMapper: UserOutstandingBalanceStatusMapperDtoToUserOutstandingBalanceStatusMapper
) : UserRepository {
    override suspend fun getUserOutstandingBalanceStatus(): UserOutstandingBalanceStatus {
        val result = userApi.getUserOutstandingBalanceStatus()
        return userOutstandingBalanceStatusMapper.map(result)
    }

    override suspend fun closeUserAccount(closeUserAccountReasonRequest: CloseUserAccountReasonRequest) {
        userApi.closeUserAccount(
            closeUserAccountReasonRequestMapper.map(closeUserAccountReasonRequest)
        )
    }

    override suspend fun pauseUserAccount() {
        userApi.pauseUserAccount()
    }

    override suspend fun getUserProfile(): UserProfile {
        val result = userApi.getUserProfile()
        return userProfileMapper.map(result)
    }

    override suspend fun getUserBankTransactions(
        paginationRequest: PaginationRequest
    ): List<BankTransaction> {
        val result = userApi.getUserBankTransaction(
            paginationRequest.page, paginationRequest.perPage
        )
        return result.map { bankTransactionMapper.map(it) }
    }

    override suspend fun updateUserProfileSetting(userSetting: UserSetting): UserSetting {
        val result = userApi.updateUserProfileSetting(userSettingToDtoMapper.map(userSetting))
        return userSettingMapper.map(result)
    }

    override suspend fun submitChangeIdentifiers(changeIdentifiersRequest: IdentifiersRequest) {
        userApi.submitChangeIdentifiers(changeIdentifiersRequestMapper.map(changeIdentifiersRequest))
    }

    override suspend fun updateChangeIdentifiers(
        updateIdentifiersRequest: UpdateIdentifiersCodeValidationRequest
    ): UserProfile {
        val result = userApi.updateChangeIdentifiers(
            updateIdentifiersRequestMapper.map(updateIdentifiersRequest)
        )
        return userProfileMapper.map(result)
    }

    override suspend fun updateUserProfile(updateUserProfileRequest: UpdateUserProfileRequest): UserProfile {
        val result = userApi.updateUserProfile(
            updateUserProfileRequestMapper.map(updateUserProfileRequest)
        )
        return userProfileMapper.map(result)
    }

    override suspend fun unpauseAccount() = userApi.unpauseUserAccount()
}
