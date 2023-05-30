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
import com.wolfpackdigital.cashli.domain.entities.response.UserProfile
import com.wolfpackdigital.cashli.domain.usecases.CloseUserAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.CompleteUpdateLinkingBankAccountUseCase
import com.wolfpackdigital.cashli.domain.usecases.GenerateUpdateLinkTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.GetEligibilityStatusUseCase
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
import com.wolfpackdigital.cashli.presentation.shared.PauseOrCloseAccountViewModel
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.initTimer
import com.wolfpackdigital.cashli.shared.utils.extensions.toFormattedLocalDateTime
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDateTime

private const val SUM_150 = "150"
private const val TRANSACTIONS_PAGE_SIZE = 10

// Span actions
private const val VALUE_SPAN_OPEN_RESOLVE_CONNECTION = "openResolveConnection"
private const val VALUE_SPAN_OPEN_INELIGIBILITY_SCREEN = "openIneligibilityScreen"

@Suppress("LongParameterList")
class HomeViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserSettingUseCase: UpdateUserSettingUseCase,
    private val getEligibilityStatusUseCase: GetEligibilityStatusUseCase,
    private val generateUpdateLinkTokenUseCase: GenerateUpdateLinkTokenUseCase,
    private val completeUpdateLinkingBankAccountUseCase: CompleteUpdateLinkingBankAccountUseCase,
    pauseUserAccountUseCase: PauseUserAccountUseCase,
    closeUserAccountUseCase: CloseUserAccountUseCase,
    getUserOutstandingBalanceStatusUseCase: GetUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase: UnpauseAccountUseCase
) : PauseOrCloseAccountViewModel(
    pauseUserAccountUseCase,
    closeUserAccountUseCase,
    getUserOutstandingBalanceStatusUseCase,
    unpauseAccountUseCase
),
    PersistenceService,
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

    fun getUserProfile() {
        performApiCall {
            val result = getUserProfileUseCase(Unit)
            result.onSuccess { newUserProfile ->
                userProfile = token?.let { newUserProfile.copy(tokens = it) }
                _currentUserProfile.value = userProfile
                if (newUserProfile.connectionExpired) {
                    handleConnectionLostInfo()
                }
                handleLinkBankAccountInfo()
                handleRequestCashAdvanceInfo()
            }
            result.onError {
                val error = it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    private fun handleConnectionLostInfo() {
        val homeWarningInfo = currentUserProfile.value?.let {
            val connectionLostSpanAction: List<TextSpanAction> = listOf(
                TextSpanAction(
                    actionKey = VALUE_SPAN_OPEN_RESOLVE_CONNECTION,
                    spanTextColor = R.color.colorPrimaryDark,
                    isSpanTextUnderlined = true,
                    isSpanTextBold = true,
                    action = { generateBankAccountUpdateLinkToken() }
                )
            )
            generateWarningInfoContent(R.string.warning_lost_connection, connectionLostSpanAction)
        }
        _cmd.value = Command.ConnectionLostWarningInfo(homeWarningInfo)
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

                userProfile.bankAccount == null &&
                    userProfile.eligibilityStatus == EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED -> {
                    LinkBankAccountInfo(
                        bankAccountInfoType = BankAccountInfoType.NOT_CONNECTED,
                        linkBankAccountAction = { goToLinkBankAccount() }
                    )
                }

                else -> {
                    LinkBankAccountInfo(
                        bankAccountInfoType = BankAccountInfoType.CONNECTED,
                        bankAccount = userProfile.bankAccount?.copy(
                            timestamp = userProfile.bankAccount.timestamp.toFormattedLocalDateTime()
                                ?: EMPTY_STRING
                        )
                    )
                }
            }
        }

        _cmd.value = Command.RefreshLinkBankAccountInfo(bankInfo)
    }

    @Suppress("LongMethod")
    private fun handleRequestCashAdvanceInfo() {
        // TODO refactor after BE response update
        val cashAdvanceInfo = currentUserProfile.value?.let { userProfile ->
            val isAccountPaused = userProfile.accountStatus == AccountStatus.PAUSED
            when {
                userProfile.eligibilityStatus == EligibilityStatus.BANK_ACCOUNT_NOT_CONNECTED ||
                    userProfile.eligibilityStatus == EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> {
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CASH_UP_TO,
                        upToSum = SUM_150
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.ELIGIBLE -> {
                    // TODO add already claimed cash check
                    val eligibilityDate = if (isAccountPaused)
                        LocalDateTime.now().toString().toFormattedLocalDateTime() ?: EMPTY_STRING
                    else
                        null

                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.APPROVED_FOR,
                        cashApproved = "$123.12",
                        isClaimCashEnabled = !userProfile.connectionExpired,
                        isAccountPaused = isAccountPaused,
                        eligibilityDate = eligibilityDate,
                        buttonAction = {
                            if (isAccountPaused) {
                                showUnpauseAccountDialog()
                            } else {
                                goToClaimCash()
                            }
                        }
                    )
                }

                userProfile.eligibilityStatus == EligibilityStatus.NOT_ELIGIBLE -> {
                    val warningInfo =
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

                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.NOT_ELIGIBLE,
                        isAccountPaused = isAccountPaused,
                        warningInfo = warningInfo,
                        buttonAction = {
                            if (isAccountPaused) {
                                showUnpauseAccountDialog()
                            } else {
                                goToIneligibleScreen()
                            }
                        }

                    )
                }

                else -> {
                    // TODO add already claimed cash check
                    RequestCashAdvanceInfo(
                        requestCashAdvanceType = RequestCashAdvanceType.CLAIMED_ADVANCE,
                        cashAdvanceBalance = "-$123.44",
                        isAccountPaused = isAccountPaused,
                        repaymentDate = LocalDateTime.now().toString().toFormattedLocalDateTime()
                            ?: EMPTY_STRING,
                        buttonAction = {
                            if (isAccountPaused) {
                                showUnpauseAccountDialog()
                            }
                        }
                    )
                }
            }
        }
        _cmd.value = Command.RefreshRequestCashAdvanceInfo(cashAdvanceInfo)
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
                _cmd.value = Command.RemoveConnectionLostWarning
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

    fun goToClaimCash() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            HomeGraphDirections.actionGlobalClaimCashGraph()
        )
    }

    private fun initCheckEligibilityStatusJob() {
        // TODO replace eligibility delay with minutes after more tests
        checkEligibilityStatusJob = initTimer(Constants.COUNT_DOWN_TIME_30_SEC).onCompletion {
            if (it == null)
                handleEligibilityStatus()
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

    @Suppress("MagicNumber")
    private fun handleEligibilityStatus() {
        viewModelScope.launch {
            val result = getEligibilityStatusUseCase(Unit)
            result.onSuccess { eligibilityStatus ->
                when (eligibilityStatus.status) {
                    EligibilityStatus.ELIGIBILITY_CHECK_PENDING -> {
                        toggleEligibilityStatusJob()
                    }

                    EligibilityStatus.ELIGIBLE, EligibilityStatus.NOT_ELIGIBLE -> {
                        getUserProfile()
                    }

                    else -> {
                        // TODO add logic if necessary
                    }
                }
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

        data class ConnectionLostWarningInfo(
            val genericWarningInfo: GenericWarningInfo?
        ) : Command()

        data class StartUpdatingLinkBankAccount(
            val linkTokenConfiguration: LinkTokenConfiguration
        ) : Command()

        object CheckPushNotificationPermissions : Command()

        object RemoveConnectionLostWarning : Command()
    }
}
