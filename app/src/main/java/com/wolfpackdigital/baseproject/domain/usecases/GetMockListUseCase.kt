package com.wolfpackdigital.baseproject.domain.usecases

import com.wolfpackdigital.baseproject.domain.entities.MockItem
import com.wolfpackdigital.baseproject.domain.abstractions.MockRepository
import com.wolfpackdigital.baseproject.shared.base.BaseUseCase
import com.wolfpackdigital.baseproject.shared.base.Result
import com.wolfpackdigital.baseproject.shared.utils.extensions.getParsedError

class GetMockListUseCase(private val repo: MockRepository) : BaseUseCase<Unit, List<MockItem>>() {
    override suspend fun run(params: Unit): Result<List<MockItem>> {
        return try {
            Result.Success(repo.getMockList())
        } catch (throwable: Throwable) {
            Result.Error(throwable.getParsedError())
        }
    }
}
