package com.wolfpackdigital.cashli.domain.usecases

import com.wolfpackdigital.cashli.domain.abstractions.MockRepository
import com.wolfpackdigital.cashli.domain.entities.MockItem
import com.wolfpackdigital.cashli.shared.base.BaseUseCase
import com.wolfpackdigital.cashli.shared.base.Result
import com.wolfpackdigital.cashli.shared.utils.extensions.getParsedError

class GetMockListUseCase(private val repo: MockRepository) : BaseUseCase<Unit, List<MockItem>>() {
    override suspend fun run(params: Unit): Result<List<MockItem>> {
        return try {
            Result.Success(repo.getMockList())
        } catch (throwable: Throwable) {
            Result.Error(throwable.getParsedError())
        }
    }
}
