package com.wolfpackdigital.cashli.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wolfpackdigital.cashli.HomeGraphDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.data.paging.BankTransactionsPagingSource
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.entities.response.UserSetting
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserSettingUseCase
import com.wolfpackdigital.cashli.presentation.entities.LinkBankAccountInfo
import com.wolfpackdigital.cashli.presentation.entities.RequestCashAdvanceInfo
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.RequestCashAdvanceType
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.Constants.PUSH_NOTIFICATION_SETTING
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDateTime
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime

private const val SUM_150 = "150"
private const val TRANSACTIONS_PAGE_SIZE = 10

class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserSettingUseCase: UpdateUserSettingUseCase
) : BaseViewModel(), PersistenceService, KoinComponent {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar, isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private val _currentUserProfile = MutableLiveData<UserProfile?>()
    val currentUserProfile: LiveData<UserProfile?> = _currentUserProfile

    val bankTransactionsFlow = Pager(
        config = PagingConfig(
            pageSize = TRANSACTIONS_PAGE_SIZE,
            enablePlaceholders = false,
            initialLoadSize = TRANSACTIONS_PAGE_SIZE
        )
    ) {
        inject<BankTransactionsPagingSource>().value
    }.flow.cachedIn(viewModelScope)

    init {
        if (!isNotificationPermissionAsked) viewModelScope.launch(Dispatchers.Main) {
            isNotificationPermissionAsked = true
            _cmd.value = Command.CheckPushNotificationPermissions
        }
    }

    fun getUserProfile() {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
                _currentUserProfile.value = userProfile
                handleLinkBankAccountInfo()
                handleRequestCashAdvanceInfo()
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun handleLinkBankAccountInfo() {
        val bankInfo = currentUserProfile.value?.let { userProfile ->
            // TODO check for pending status
            LinkBankAccountInfo(
                bankAccount = userProfile.bankAccount?.copy(
                    timestamp = userProfile.bankAccount.timestamp.toFormattedLocalDateTime()
                        ?: EMPTY_STRING
                ),
                linkBankAccountAction = { goToLinkBankAccount() }
            )
        }
        _cmd.value = Command.RefreshLinkBankAccountInfo(bankInfo)
    }

    private fun handleRequestCashAdvanceInfo() {
        val cashAdvanceInfo = currentUserProfile.value?.let { userProfile ->
            when {
                userProfile.eligibilityStatus == EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED ||
                    userProfile.eligibilityStatus == EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> {
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CASH_UP_TO,
                        eligibilityStatus = userProfile.eligibilityStatus,
                        upToSum = SUM_150
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.ELIGIBLE -> {
                    // TODO add already claimed cash check
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.APPROVED_FOR,
                        cashApproved = "$123.12",
                        claimCashNowAction = { goToClaimCash() }
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.NOT_ELIGIBLE -> RequestCashAdvanceInfo(
                    requestCashAdvanceType = RequestCashAdvanceType.NOT_ELIGIBLE,
                    seeMoreAction = {
                        _baseCmd.value = BaseCommand.PerformNavDeepLink(
                            deepLink = Constants.INELIGIBLE_INFORMATIVE_SCREEN_DL
                        )
                    }
                )

                else -> {
                    // TODO add already claimed cash check
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CLAIMED_ADVANCE,
                        cashAdvanceBalance = "-$123.44",
                        repaymentDate = LocalDateTime.now().toString().toFormattedLocalDateTime()
                            ?: EMPTY_STRING
                    )
                }
            }
        }
        _cmd.value = Command.RefreshRequestCashAdvanceInfo(cashAdvanceInfo)
    }

    fun handleUserPushNotificationsSetting(isGranted: Boolean) {
        hasNotificationPermissionGranted = isGranted
        if (!isGranted) _baseCmd.value = BaseCommand.ShowToast(R.string.notification_rejected_text)
        performApiCall(showLoading = false) {
            val result = updateUserSettingUseCase(
                UserSetting(
                    key = PUSH_NOTIFICATION_SETTING, value = isGranted.toString()
                )
            )
            result.onSuccess { newUserSettings ->
                userProfile =
                    userProfile?.copy(
                        userSettings = userProfile?.userSettings?.map { oldUserSettings ->
                            if (newUserSettings.key == oldUserSettings.key) newUserSettings
                            else oldUserSettings
                        } ?: listOf()
                    )
            }
        }
    }

    private fun goToLinkBankAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeGraphDirections.actionGlobalLinkAccountGraph()
        )
    }

    fun goToClaimCash() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeGraphDirections.actionGlobalClaimCashGraph()
        )
    }

    sealed class Command {
        data class RefreshRequestCashAdvanceInfo(
            val requestCashAdvanceInfo: RequestCashAdvanceInfo?
        ) : Command()

        data class RefreshLinkBankAccountInfo(
            val linkBankAccountInfo: LinkBankAccountInfo?
        ) : Command()

        object CheckPushNotificationPermissions : Command()
    }
}
