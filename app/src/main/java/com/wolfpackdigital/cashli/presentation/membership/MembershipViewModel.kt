package com.wolfpackdigital.cashli.presentation.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.data.paging.CashAdvanceHistoryPagingSource
import com.wolfpackdigital.cashli.domain.entities.enums.CashAdvanceStatus
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants.DASH
import com.wolfpackdigital.cashli.shared.utils.Constants.TRANSACTIONS_PAGE_SIZE
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDate
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedZonedDate
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MembershipViewModel : BaseViewModel(), PersistenceService, KoinComponent {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.membership_advance_history,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val cashAdvanceHistoryFlow = Pager(
        config = PagingConfig(
            pageSize = TRANSACTIONS_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = TRANSACTIONS_PAGE_SIZE
        )
    ) {
        inject<CashAdvanceHistoryPagingSource>().value
    }.flow
        .map { pagingData ->
            pagingData.map { cashAdvanceDetails ->
                when (cashAdvanceDetails.cashAdvanceStatus) {
                    CashAdvanceStatus.OVERDUE ->
                        MembershipAdvanceItem.AdvanceOverdueItem(
                            topText = cashAdvanceDetails.amount,
                            middleText = cashAdvanceDetails.dueDate.toFormattedZonedDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedZonedDate() ?: DASH
                        )
                    CashAdvanceStatus.PAID ->
                        MembershipAdvanceItem.AdvancePaidItem(
                            topText = cashAdvanceDetails.amount,
                            middleText = cashAdvanceDetails.dueDate.toFormattedZonedDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedZonedDate() ?: DASH
                        )
                    CashAdvanceStatus.SCHEDULED ->
                        MembershipAdvanceItem.AdvanceScheduledItem(
                            topText = cashAdvanceDetails.amount,
                            middleText = cashAdvanceDetails.dueDate.toFormattedZonedDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedZonedDate() ?: DASH
                        )
                }
            }
        }
        .cachedIn(viewModelScope)

    private val _membershipItem = MutableLiveData(
        userProfile?.lastMembership?.let { lastMembership ->
            when (lastMembership.isMembershipActive) {
                true -> MembershipAdvanceItem.MembershipActiveItem(
                    topText = lastMembership.startDate.toFormattedLocalDate() ?: DASH,
                    middleText = lastMembership.endDate.toFormattedLocalDate() ?: DASH,
                    bottomText = lastMembership.amount
                )

                false -> MembershipAdvanceItem.MembershipPausedItem(
                    topText = lastMembership.startDate.toFormattedLocalDate() ?: DASH,
                    middleText = lastMembership.endDate.toFormattedLocalDate() ?: DASH,
                    bottomText = lastMembership.amount
                )
            }
        }
    )
    val membershipItem: LiveData<MembershipAdvanceItem?> = _membershipItem
}
