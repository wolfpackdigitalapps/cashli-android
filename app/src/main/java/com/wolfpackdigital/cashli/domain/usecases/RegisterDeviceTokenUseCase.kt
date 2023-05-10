package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.AuthRepository
import com.wolfpackdigital.cashli.domain.entities.requests.SingleDataRequest
import com.wolfpackdigital.cashli.shared.base.BaseMessagingService
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class RegisterDeviceTokenUseCase(private val repo: AuthRepository) :
    BaseUseCase<Unit, Unit>(), PersistenceService {
    override suspend fun run(params: Unit): Result<Unit> {
        val fcmDeviceToken = BaseMessagingService.getDeviceToken()
        return Result.Success(repo.registerDeviceToken(SingleDataRequest(fcmDeviceToken))).also {
            deviceToken = fcmDeviceToken
        }
    }
}
