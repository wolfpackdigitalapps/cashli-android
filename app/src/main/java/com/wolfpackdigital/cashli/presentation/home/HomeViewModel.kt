@file:Suppress("TooManyFunctions")

package com.wolfpackdigital.cashli.presentation.home

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.plaid.link.configuration.LinkTokenConfiguration
import com.plaid.link.linkTokenConfiguration
import com.plaid.link.result.LinkExit
import com.plaid.link.result.LinkSuccess
import com.wolfpackdigital.cashli.HomeGraphDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.data.paging.BankTransactionsPagingSource
import com.wolfpackdigital.cashli.domain.entities.UserSetting
import com.wolfpackdigital.cashli.domain.entities.enums.AccountStatus
import com.wolfpackdigital.cashli.domain.entities.enums.EligibilityStatus
import com.wolfpackdigital.cashli.domain.entities.enums.UserSettingsKeys
import com.wolfpackdigital.cashli.domain.entities.requests.CompleteLinkBankAccountRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountBalanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountInfoRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountLocalizedBalanceRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountMetadataRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountSubtypeRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountTypeRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkAccountVerificationStatusRequest
import com.wolfpackdigital.cashli.domain.entities.requests.linkBankAccount.LinkInstitutionRequest
import com.wolfpackdigital.cashli.domain.entities.response.EligibilityChecks
import com.wolfpackdigital.cashli.domain.entities.response.UserOutstandingBalanceStatus
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.CompleteUpdateLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateUpdateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetCashAdvancesLimitsUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserOutstandingBalanceStatusUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.PauseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.UnpauseAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserSettingUseCase
import com.wolfpackdigital.cashli.presentation.entities.GenericWarningInfo
import com.wolfpackdigital.cashli.presentation.entities.LinkBankAccountInfo
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.RequestCashAdvanceInfo
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.BankAccountInfoType
import com.wolfpackdigital.cashli.presentation.entities.enums.RequestCashAdvanceType
import com.wolfpackdigital.cashli.presentation.entities.enums.WarningInfoType
import com.wolfpackdigital.cashli.presentation.shared.PauseOrCloseAccountViewModel
import com.wolfpackdigital.cashli.shared.base.ApiError
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.Result
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.DASH
import com.wolfpackdigital.cashli.shared.utils.Constants.TRANSACTIONS_PAGE_SIZE
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.initTimer
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDate
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedZonedDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private const val SUM_150 = "150"

// Span actions
private const val VALUE_SPAN_OPEN_RESOLVE_CONNECTION = "openResolveConnection"
private const val VALUE_SPAN_OPEN_INELIGIBILITY_SCREEN = "openIneligibilityScreen"
private const val VALUE_SPAN_CALL_US = "callUs"

@Suppress("LongParameterList")
class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserSettingUseCase: UpdateUserSettingUseCase,
    private val getCashAdvancesLimitsUseCase: GetCashAdvancesLimitsUseCase,
    private val generateUpdateLinkTokenUseCase: GenerateUpdateLinkTokenUseCase,
    private val completeUpdateLinkingBankAccountUseCase: CompleteUpdateLinkingBankAccountUseCase,
    pauseUserAccountUseCase: PauseUserAccountUseCase,
    closeUserAccountUseCase: CloseUserAccountUseCase,
    private val getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase: UnpauseAccountUseCase
) : PauseOrCloseAccountViewModel(
    pauseUserAccountUseCase,
    closeUserAccountUseCase,
    getUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase
),
    KoinComponent {

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

    private var outstandingBalance: UserOutstandingBalanceStatus? = null
    private var cashAdvanceLimits: EligibilityChecks? = null

    private val _accountWarnings = MutableLiveData<List<GenericWarningInfo>?>()
    val accountWarnings: LiveData<List<GenericWarningInfo>?> = _accountWarnings

    private var checkEligibilityStatusJob: Job? = null

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

    fun getUserProfile(eligibilityChecks: EligibilityChecks? = null) {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
                _currentUserProfile.value = userProfile

                handleWarningInfo(
                    type = WarningInfoType.BANK_CONNECTION_LOST,
                    condition = newUserProfile.connectionExpired
                )
                handleWarningInfo(
                    type = WarningInfoType.ACCOUNT_SUSPENDED,
                    condition = newUserProfile.suspended
                )

                handleLinkBankAccountInfo()

                handleRequestCashAdvanceEligibility(eligibilityChecks)

                handleRequestCashAdvanceInfo()
            }
            result.onError {
                handleDefaultErrors(it)
            }
        }
    }

    private suspend fun handleRequestCashAdvanceEligibility(eligibilityChecks: EligibilityChecks? = null) {
        withContext(Dispatchers.Default) {
            val outstandingDef = async { getUserOutstandingBalanceStatusUseCase(Unit) }
            val eligibilityChecksDef =
                if (eligibilityChecks == null)
                    async { getCashAdvancesLimitsUseCase(Unit) }
                else
                    null

            val outstandingResult = outstandingDef.await()
            val eligibilityChecksResult = eligibilityChecksDef?.await()

            when {
                outstandingResult is Result.Error -> {
                    handleDefaultErrors(outstandingResult.exception)
                }

                eligibilityChecksResult is Result.Error -> {
                    handleDefaultErrors(eligibilityChecksResult.exception)
                }

                else -> {
                    outstandingBalance = (outstandingResult as Result.Success).data
                    cashAdvanceLimits =
                        eligibilityChecks ?: (eligibilityChecksResult as Result.Success).data
                }
            }
        }
    }

    private fun handleDefaultErrors(it: ApiError) {
        val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
        _baseCmd.value = BaseCommand.ShowToast(error)
    }

    private fun handleWarningInfo(type: WarningInfoType, condition: Boolean) {
        _accountWarnings.value = when (type) {
            WarningInfoType.BANK_CONNECTION_LOST -> {
                generateWarningsByType(
                    condition,
                    R.string.warning_lost_connection,
                    ::handleConnectionLostWarningInfo
                ) ?: return
            }

            WarningInfoType.ACCOUNT_SUSPENDED -> {
                generateWarningsByType(
                    condition,
                    R.string.warning_account_suspended,
                    ::handleAccountSuspendedWarningInfo
                ) ?: return
            }
        }
    }

    private fun generateWarningsByType(
        condition: Boolean,
        @StringRes warningTextId: Int,
        warningInfoGenerator: () -> GenericWarningInfo
    ): List<GenericWarningInfo>? {
        val warning = accountWarnings.value?.find { warningInfo ->
            warningInfo.warningTextId == warningTextId
        }
        return when {
            (warning == null && !condition) -> null
            else -> {
                mutableListOf<GenericWarningInfo>().apply {
                    addAll(accountWarnings.value ?: emptyList())
                    when {
                        warning == null ->
                            add(warningInfoGenerator())

                        !condition ->
                            removeIf { warningInfo ->
                                warningInfo.warningTextId == warningTextId
                            }
                    }
                }
            }
        }
    }

    private fun handleConnectionLostWarningInfo(): GenericWarningInfo {
        val connectionLostSpanAction: List<TextSpanAction> = listOf(
            TextSpanAction(
                actionKey = VALUE_SPAN_OPEN_RESOLVE_CONNECTION,
                spanTextColor = R.color.colorPrimaryDark,
                isSpanTextUnderlined = true,
                isSpanTextBold = true,
                action = ::generateBankAccountUpdateLinkToken
            )
        )
        return generateWarningInfoContent(
            R.string.warning_lost_connection,
            connectionLostSpanAction
        )
    }

    private fun handleAccountSuspendedWarningInfo(): GenericWarningInfo {
        val connectionLostSpanAction: List<TextSpanAction> = listOf(
            TextSpanAction(
                actionKey = VALUE_SPAN_CALL_US,
                spanTextColor = R.color.colorPrimaryDark,
                isSpanTextUnderlined = true,
                isSpanTextBold = true,
                action = {
                    _baseCmd.value = BaseCommand.OpenSMSApp()
                }
            )
        )
        return generateWarningInfoContent(
            R.string.warning_account_suspended,
            connectionLostSpanAction
        )
    }

    private fun generateWarningInfoContent(@StringRes textId: Int, actions: List<TextSpanAction>?) =
        GenericWarningInfo(
            warningTextId = textId,
            spanActions = actions
        )

    private fun handleLinkBankAccountInfo() {
        val bankInfo = currentUserProfile.value?.let { userProfile ->
            when {
                userProfile.eligibilityStatus == EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> {
                    toggleEligibilityStatusJob()
                    LinkBankAccountInfo(bankAccountInfoType = BankAccountInfoType.PENDING)
                }

                userProfile.eligibilityStatus == EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED ||
                    !userProfile.bankAccountConnected -> {
                    LinkBankAccountInfo(
                        bankAccountInfoType = BankAccountInfoType.NOT_CONNECTED,
                        bankAccount = userProfile.bankAccount,
                        linkBankAccountAction = { goToLinkBankAccount() }
                    )
                }

                else -> {
                    LinkBankAccountInfo(
                        bankAccountInfoType = BankAccountInfoType.CONNECTED,
                        bankAccount = userProfile.bankAccount
                    )
                }
            }
        }

        _cmd.value = Command.RefreshLinkBankAccountInfo(bankInfo)
    }

    private fun handleRequestCashAdvanceInfo() {
        val cashAdvanceInfo = safeLet(
            currentUserProfile.value,
            outstandingBalance,
            cashAdvanceLimits
        ) { userProfile, outstandingBalance, cashAdvanceLimits ->
            val isAccountPaused = userProfile.accountStatus == AccountStatus.PAUSED
            when {
                outstandingBalance.outstandingBalance && outstandingBalance.repaymentDate != null -> {
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CLAIMED_ADVANCE,
                        cashAdvanceBalance = "-${outstandingBalance.cashAdvanceBalanceDue}",
                        isAccountPaused = isAccountPaused,
                        repaymentDate = outstandingBalance.repaymentDate.toFormattedLocalDate()
                            ?: DASH,
                        buttonAction = {
                            if (isAccountPaused) {
                                showUnpauseAccountDialog(R.id.homeFragment)
                            }
                        }
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED ||
                    userProfile.eligibilityStatus == EligibilityStatus.ELIGIBILITY_CHECK_PENDING ||
                    !userProfile.bankAccountConnected -> {
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CASH_UP_TO,
                        upToSum = cashAdvanceLimits.maxCashAdvance?.toString() ?: SUM_150
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.ELIGIBLE -> {
                    handleUserEligibleForCashAdvance(
                        isAccountPaused,
                        userProfile,
                        cashAdvanceLimits
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.NOT_ELIGIBLE -> {
                    handleUserNotEligibleForCashAdvance(isAccountPaused)
                }

                else -> null
            }
        }
        _cmd.value = Command.RefreshRequestCashAdvanceInfo(cashAdvanceInfo)
    }

    private fun handleUserEligibleForCashAdvance(
        isAccountPaused: Boolean,
        userProfile: UserProfile,
        cashAdvanceLimits: EligibilityChecks
    ): RequestCashAdvanceInfo {
        val eligibilityDate =
            if (isAccountPaused)
                userProfile.becameEligibleAt?.toFormattedZonedDate() ?: DASH
            else
                null

        val isActionButtonEnabled = when {
            isAccountPaused -> true
            userProfile.connectionExpired || userProfile.suspended -> false
            else -> true
        }

        return RequestCashAdvanceInfo(
            requestCashAdvanceType = RequestCashAdvanceType.APPROVED_FOR,
            cashApproved = cashAdvanceLimits.userMaxAdvanceFormatted,
            isActionButtonEnabled = isActionButtonEnabled,
            isAccountPaused = isAccountPaused,
            eligibilityDate = eligibilityDate,
            buttonAction = {
                if (isAccountPaused) {
                    showUnpauseAccountDialog(R.id.homeFragment)
                } else {
                    goToClaimCash()
                }
            }
        )
    }

    private fun handleUserNotEligibleForCashAdvance(isAccountPaused: Boolean): RequestCashAdvanceInfo {
        val warningInfo =
            handleNotEligibleExplanationSection(isAccountPaused)

        return RequestCashAdvanceInfo(
            requestCashAdvanceType = RequestCashAdvanceType.NOT_ELIGIBLE,
            isAccountPaused = isAccountPaused,
            warningInfo = warningInfo,
            buttonAction = {
                if (isAccountPaused) {
                    showUnpauseAccountDialog(R.id.homeFragment)
                } else {
                    goToIneligibleScreen()
                }
            }

        )
    }

    private fun handleNotEligibleExplanationSection(isAccountPaused: Boolean) =
        when (isAccountPaused) {
            true ->
                generateWarningInfoContent(
                    textId = R.string.can_not_get_cash_advance_see_more,
                    actions = listOf(
                        TextSpanAction(
                            actionKey = VALUE_SPAN_OPEN_INELIGIBILITY_SCREEN,
                            spanTextColor = R.color.colorWhite,
                            isSpanTextUnderlined = true,
                            isSpanTextBold = false,
                            action = { goToIneligibleScreen() }
                        )
                    )
                )

            false -> generateWarningInfoContent(
                textId = R.string.can_not_get_cash_advance,
                actions = null
            )
        }

    private fun goToIneligibleScreen() {
        _baseCmd.value = BaseCommand.PerformNavDeepLink(
            deepLink = Constants.INELIGIBLE_INFORMATIVE_SCREEN_DL
        )
    }

    fun handleUserPushNotificationsSetting(isGranted: Boolean) {
        hasNotificationPermissionGranted = isGranted
        if (!isGranted) _baseCmd.value = BaseCommand.ShowToast(R.string.notification_rejected_text)
        performApiCall(showLoading = false) {
            val result = updateUserSettingUseCase(
                UserSetting(
                    key = UserSettingsKeys.PUSH_NOTIFICATIONS_ENABLED,
                    value = isGranted.toString()
                )
            )
            result.onSuccess { newUserSetting ->
                userProfile =
                    userProfile?.copy(
                        userSettings = userProfile?.userSettings?.find {
                            it.key == newUserSetting.key
                        }?.let {
                            handleUpdateExistingUserSetting(newUserSetting)
                        } ?: handleNewUserSetting(newUserSetting)
                    )
                userProfile =
                    userProfile?.copy(
                        userSettings = userProfile?.userSettings?.map { oldUserSetting ->
                            if (newUserSetting.key == oldUserSetting.key) newUserSetting
                            else oldUserSetting
                        } ?: listOf()
                    )
            }
        }
    }

    private fun handleUpdateExistingUserSetting(newUserSetting: UserSetting) =
        userProfile?.userSettings?.map { oldUserSetting ->
            if (newUserSetting.key == oldUserSetting.key) newUserSetting
            else oldUserSetting
        } ?: listOf()

    private fun handleNewUserSetting(newUserSetting: UserSetting) =
        buildList {
            userProfile?.userSettings?.let { addAll(it) }
            add(newUserSetting)
        }

    private fun generateBankAccountUpdateLinkToken() {
        performApiCall {
            val result = generateUpdateLinkTokenUseCase(Unit)
            result.onSuccess { bankToken ->
                val linkTokenConfiguration = linkTokenConfiguration {
                    token = bankToken.linkToken
                }
                _cmd.value = Command.StartUpdatingLinkBankAccount(linkTokenConfiguration)
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun onSuccessUpdatingLinkBankAccount(linkSuccess: LinkSuccess) {
        performApiCall {
            val request = createLinkBankAccountRequest(linkSuccess)
            val result = completeUpdateLinkingBankAccountUseCase(request)
            result.onSuccess {
                handleWarningInfo(
                    type = WarningInfoType.BANK_CONNECTION_LOST,
                    condition = false
                )
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun onFailUpdatingLinkBankAccount(linkFail: LinkExit) {
        linkFail.error?.let {
            handlePlaidErrorPopup(
                titleId = R.string.connection_failed,
                contentIdOrString = R.string.bank_account_connection_fail
            )
        }
    }

    private fun handlePlaidErrorPopup(@StringRes titleId: Int, contentIdOrString: Any?) {
        _baseCmd.value = BaseCommand.ShowPopupById(
            PopupConfig(
                titleIdOrString = titleId,
                contentIdOrString = contentIdOrString,
                imageId = R.drawable.ic_warning,
                isCloseVisible = false,
                buttonPrimaryId = R.string.go_back_to_home,
                buttonSecondaryId = R.string.get_support,
                buttonSecondaryClick = {
                    _baseCmd.value = BaseCommand.OpenSMSApp()
                }
            )
        )
    }

    private fun createLinkBankAccountRequest(linkSuccess: LinkSuccess) =
        CompleteLinkBankAccountRequest(
            publicToken = linkSuccess.publicToken,
            metadata = LinkAccountMetadataRequest(
                accounts = linkSuccess.metadata.accounts.map { linkAccount ->
                    LinkAccountInfoRequest(
                        id = linkAccount.id,
                        name = linkAccount.name,
                        mask = linkAccount.mask,
                        subtype = LinkAccountSubtypeRequest(
                            name = linkAccount.subtype.json,
                            type = LinkAccountTypeRequest(
                                name = linkAccount.subtype.accountType.json
                            )
                        ),
                        verificationStatus = LinkAccountVerificationStatusRequest(
                            name = linkAccount.verificationStatus?.json
                        ),
                        balance = LinkAccountBalanceRequest(
                            available = linkAccount.balance?.available,
                            currency = linkAccount.balance?.currency,
                            current = linkAccount.balance?.current,
                            localized = LinkAccountLocalizedBalanceRequest(
                                available = linkAccount.balance?.localized?.available,
                                current = linkAccount.balance?.localized?.current
                            )
                        )
                    )
                },
                institution = LinkInstitutionRequest(
                    id = linkSuccess.metadata.institution?.id,
                    name = linkSuccess.metadata.institution?.name
                ),
                linkSessionId = linkSuccess.metadata.linkSessionId,
                metadataJson = linkSuccess.metadata.metadataJson
            )
        )

    private fun goToLinkBankAccount() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeGraphDirections.actionGlobalLinkAccountGraph()
        )
    }

    private fun goToClaimCash() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeGraphDirections.actionGlobalClaimCashGraph()
        )
    }

    private fun initCheckEligibilityStatusJob() {
        // TODO replace eligibility delay with minutes after more tests
        checkEligibilityStatusJob = initTimer(Constants.COUNT_DOWN_TIME_30_SEC).onCompletion {
            if (it == null)
                viewModelScope.launch {
                    handleEligibilityStatus(
                        onEligibleCallback = { eligibilityChecks ->
                            getUserProfile(eligibilityChecks)
                        },
                        onNotEligibleCallback = ::getUserProfile,
                        onPendingCallback = ::toggleEligibilityStatusJob
                    )
                }
            cancelCheckEligibilityStatusJob()
        }.launchIn(viewModelScope)
    }

    private fun toggleEligibilityStatusJob() {
        viewModelScope.launch {
            checkEligibilityStatusJob?.let {
                cancelCheckEligibilityStatusJob()
            }
            initCheckEligibilityStatusJob()
        }
    }

    fun cancelCheckEligibilityStatusJob() {
        checkEligibilityStatusJob?.cancel()
        checkEligibilityStatusJob = null
    }

    private suspend fun handleEligibilityStatus(
        onEligibleCallback: (EligibilityChecks) -> Unit = {},
        onNotEligibleCallback: () -> Unit = {},
        onPendingCallback: () -> Unit = {},
        onOtherCasesCallback: () -> Unit = {}
    ) {
        val result = getCashAdvancesLimitsUseCase(Unit)
        result.onSuccess { eligibility ->
            when (eligibility.status) {
                EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> onPendingCallback()

                EligibilityStatus.ELIGIBLE -> onEligibleCallback(eligibility)

                EligibilityStatus.NOT_ELIGIBLE -> onNotEligibleCallback()

                else -> onOtherCasesCallback()
            }
        }
    }

    override fun onUnpauseAccountSuccessful() {
        getUserProfile()
    }

    sealed class Command {
        data class RefreshRequestCashAdvanceInfo(
            val requestCashAdvanceInfo: RequestCashAdvanceInfo?
        ) : Command()

        data class RefreshLinkBankAccountInfo(
            val linkBankAccountInfo: LinkBankAccountInfo?
        ) : Command()

        data class StartUpdatingLinkBankAccount(
            val linkTokenConfiguration: LinkTokenConfiguration
        ) : Command()

        object CheckPushNotificationPermissions : Command()
    }
}
