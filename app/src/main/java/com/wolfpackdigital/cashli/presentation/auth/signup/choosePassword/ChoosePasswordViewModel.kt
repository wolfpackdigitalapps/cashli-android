package com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.NavigationDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BasePasswordValidatorViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.COUNT_DOWN_TIME_6s
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine

class ChoosePasswordViewModel : BasePasswordValidatorViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = Constants.STEP_3,
            isStepCounterVisible = true,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar
    val termsAndConditionsSpanActions: List<TextSpanAction> = listOf(
        TextSpanAction(
            actionKey = Constants.VALUE_SPAN_OPEN_TS,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.terms_of_service_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = Constants.VALUE_SPAN_OPEN_PP,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.privacy_policy_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = Constants.VALUE_SPAN_OPEN_DTS,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.dwolla_terms_of_service_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = Constants.VALUE_SPAN_OPEN_DPP,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.dwolla_privacy_policy_url)
            },
            spanTextColor = R.color.colorWhiteF5
        )
    )

    @StringRes
    val termsAndConditionsTextId: Int = R.string.terms_and_conditions
    val termsAccepted = MutableLiveData(false)
    val isFinishSignUpEnabled =
        combine(
            termsAccepted.asFlow(),
            password.asFlow(),
            confirmPassword.asFlow()
        ) { termsAccepted, password, confirmPassword ->
            termsAccepted && !password.isNullOrBlank() && !confirmPassword.isNullOrBlank()
        }.asLiveData()

    @Suppress("MagicNumber")
    fun onFinishSignUpClicked() {
        validatePasswords {
            // TODO add call to BE and nav to success dialog
            delay(2000)
            _baseCmd.value = BaseCommand.ShowPopupById(
                PopupConfig(
                    titleId = R.string.bravo_text,
                    contentIdOrString = R.string.account_created_successfully,
                    imageId = R.drawable.ic_profile_check,
                    timerCount = COUNT_DOWN_TIME_6s,
                    buttonCloseClick = {
                        _baseCmd.value = BaseCommand.PerformNavAction(
                            NavigationDirections.actionGlobalHomeGraph(),
                            popUpTo = R.id.navigation,
                            inclusive = true
                        )
                    }
                )
            )
        }
    }

    sealed class Command
}
