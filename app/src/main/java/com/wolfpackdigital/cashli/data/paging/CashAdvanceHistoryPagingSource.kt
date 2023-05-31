package com.wolfpackdigital.cashli.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wolfpackdigital.cashli.domain.abstractions.repositories.CashAdvanceRepository
import com.wolfpackdigital.cashli.domain.entities.requests.PaginationRequest
import com.wolfpackdigital.cashli.domain.entities.response.CashAdvanceDetails
import com.wolfpackdigital.cashli.shared.utils.Constants

@Suppress("TooGenericExceptionCaught")
class CashAdvanceHistoryPagingSource(
    private val repo: CashAdvanceRepository
) : PagingSource<Int, CashAdvanceDetails>() {
    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, CashAdvanceDetails> {
        val nextPageNumber = params.key ?: Constants.DEFAULT_PAGE_INDEX
        return try {
            val request = PaginationRequest(
                page = nextPageNumber,
                perPage = params.loadSize
            )
            val response = repo.getCashAdvancesHistory(request)
            LoadResult.Page(
                data = response,
                prevKey = null, // Only paging forward.
                nextKey = if (response.count() < params.loadSize) null else nextPageNumber.inc()
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, CashAdvanceDetails>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.inc() ?: anchorPage?.nextKey?.dec()
        }
    }
}
