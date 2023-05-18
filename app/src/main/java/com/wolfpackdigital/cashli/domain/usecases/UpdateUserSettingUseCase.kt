package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result

class UpdateUserSettingUseCase(private val repo: UserRepository) :
    BaseUseCase<UserSetting, UserSetting>() {
    override suspend fun run(params: UserSetting): Result<UserSetting> {
        return Result.Success(repo.updateUserProfileSetting(params))
    }
}
