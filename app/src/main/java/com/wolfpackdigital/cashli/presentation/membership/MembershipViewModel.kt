package com.wolfpackdigital.cashli.presentation.membership

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class MembershipViewModel : BaseViewModel(), PersistenceService {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.membership_advance_history,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private val _membershipItem = MutableLiveData<MembershipAdvanceItem?>(
        when (userProfile?.accountStatus) {
            AccountStatus.ACTIVE -> MembershipAdvanceItem.MembershipActiveItem(
                topText = String(),
                middleText = String(),
                bottomText = String()
            )

            AccountStatus.PAUSED -> MembershipAdvanceItem.MembershipPausedItem(
                topText = String(),
                middleText = String(),
                bottomText = String()
            )

            else -> null
        }
    )
    val membershipItem: LiveData<MembershipAdvanceItem?> = _membershipItem
}
