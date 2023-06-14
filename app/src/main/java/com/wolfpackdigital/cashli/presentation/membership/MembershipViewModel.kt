package com.wolfpackdigital.cashli.presentation.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.data.paging.CashAdvanceHistoryPagingSource
import com.wolfpackdigital.cashli.domain.entities.enums.CashAdvanceStatus
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants.DASH
import com.wolfpackdigital.cashli.shared.utils.Constants.TRANSACTIONS_PAGE_SIZE
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDate
import kotlinx.coroutines.flow.map
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MembershipViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : BaseViewModel(), KoinComponent {

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
                            middleText = cashAdvanceDetails.dueDate.toFormattedLocalDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedLocalDate() ?: DASH
                        )

                    CashAdvanceStatus.PAID ->
                        MembershipAdvanceItem.AdvancePaidItem(
                            topText = cashAdvanceDetails.amount,
                            middleText = cashAdvanceDetails.dueDate.toFormattedLocalDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedLocalDate() ?: DASH
                        )

                    CashAdvanceStatus.SCHEDULED ->
                        MembershipAdvanceItem.AdvanceScheduledItem(
                            topText = cashAdvanceDetails.amount,
                            middleText = cashAdvanceDetails.dueDate.toFormattedLocalDate() ?: DASH,
                            bottomText = cashAdvanceDetails.paidDate.toFormattedLocalDate() ?: DASH
                        )
                }
            }
        }
        .cachedIn(viewModelScope)

    private val currentUserProfile = MutableLiveData<UserProfile?>()

    val membershipItem = currentUserProfile.map { currentUserProfile ->
        currentUserProfile?.lastMembership?.let { lastMembership ->
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
    }

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
                currentUserProfile.value = userProfile
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }
}
