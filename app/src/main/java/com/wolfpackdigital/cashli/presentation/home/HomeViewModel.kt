package com.wolfpackdigital.cashli.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.presentation.entities.LinkBankAccountInfo
import com.wolfpackdigital.cashli.presentation.entities.RequestCashAdvanceInfo
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDateTime
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import java.time.LocalDateTime
import kotlin.random.Random

private const val SUM_150 = "150"

class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase
) : BaseViewModel(), PersistenceService {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private val _currentUserProfile = MutableLiveData(userProfile)
    val currentUserProfile: LiveData<UserProfile?> = _currentUserProfile

    val linkBankAccountInfo: LiveData<LinkBankAccountInfo> = currentUserProfile.map { userProfile ->
        LinkBankAccountInfo(
            bankAccount = userProfile?.bankAccount?.copy(
                timestamp = userProfile.bankAccount.timestamp.toFormattedLocalDateTime() ?: EMPTY_STRING
            ),
            linkBankAccountAction = { goToLinkBankAccount() }
        )
    }

    private val _requestCashAdvanceInfo = MutableLiveData<RequestCashAdvanceInfo>()
    val requestCashAdvanceInfo: LiveData<RequestCashAdvanceInfo> = _requestCashAdvanceInfo

    @Suppress("MagicNumber")
    fun mockRequestCashAdvance() {
        // TODO delete this method after BE ready
        val rand = Random(System.currentTimeMillis()).nextInt(0, 100)
        when {
            rand < 25 -> {
                _requestCashAdvanceInfo.value = RequestCashAdvanceInfo(
                    eligible = null,
                    upToSum = SUM_150
                )
            }
            rand in 25..50 -> {
                _requestCashAdvanceInfo.value = RequestCashAdvanceInfo(
                    eligible = true,
                    cashApproved = 123f
                )
            }
            rand in 51..75 -> {
                _requestCashAdvanceInfo.value = RequestCashAdvanceInfo(
                    eligible = true,
                    cashAdvanceBalance = 123f,
                    repaymentDate = LocalDateTime.now().toString().toFormattedLocalDateTime() ?: EMPTY_STRING
                )
            }
            else -> {
                _requestCashAdvanceInfo.value = RequestCashAdvanceInfo(
                    seeMoreAction = {
                        _baseCmd.value = BaseCommand.PerformNavDeepLink(
                            deepLink = Constants.INELIGIBLE_INFORMATIVE_SCREEN_DL
                        )
                    }
                )
            }
        }
        Log.d("AAAA", (_requestCashAdvanceInfo.value?.eligible == null).toString())
        Log.d("AAAA", rand.toString())
        Log.d("AAAA - v", _requestCashAdvanceInfo.value.toString())
    }

    fun getUserProfile() {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun goToLinkBankAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeFragmentDirections.actionGlobalLinkAccountGraph()
        )
    }

    sealed class Command
}
