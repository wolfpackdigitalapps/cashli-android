package com.wolfpackdigital.cashli.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolfpackdigital.cashli.domain.abstractions.repositories.UserRepository
import com.wolfpackdigital.cashli.domain.entities.requests.PaginationRequest
import com.wolfpackdigital.cashli.domain.entities.response.BankTransaction
import com.wolfpackdigital.cashli.shared.utils.Constants.DEFAULT_PAGE_INDEX

@Suppress("TooGenericExceptionCaught")
class BankTransactionsPagingSource(
    private val repo: UserRepository
) : PagingSource<Int, BankTransaction>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, BankTransaction> {
        val nextPageNumber = params.key ?: DEFAULT_PAGE_INDEX
        return try {
            val request = PaginationRequest(
                page = nextPageNumber,
                perPage = params.loadSize
            )
            val response = repo.getUserBankTransactions(request)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = if (response.count() < params.loadSize) null else nextPageNumber.inc()
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, BankTransaction>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.inc() ?: anchorPage?.nextKey?.dec()
        }
    }
}
