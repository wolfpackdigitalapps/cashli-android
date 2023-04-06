package com.wolfpackdigital.cashli.presentation.auth.validateCode

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseValidateCodeViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants.COUNT_DOWN_TIME_6s
import com.wolfpackdigital.cashli.shared.utils.Constants.STEP_1
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.delay

class ValidateCodeViewModel(
    codeReceivedViaType: CodeReceivedViaType
) : BaseValidateCodeViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = STEP_1,
            isStepCounterVisible = true,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val titleRes: Int by lazy {
        when (codeReceivedViaType) {
            CodeReceivedViaType.SMS -> R.string.code_from_sms
            CodeReceivedViaType.EMAIL -> R.string.code_from_email
        }
    }

    init {
        initResendCode()
    }

    fun getSupport() {
        _cmd.value = Command.GetSupport
    }

    @Suppress("MagicNumber")
    fun validateCode() {
        // TODO add api call and replace mock behavior
        performApiCall {
            delay(200)
            if (verificationCode.value == "4321")
                _invalidCodeErrorVisible.value = R.string.invalid_code_too_many_attempts
            else if (verificationCode.value != "1234")
                _invalidCodeErrorVisible.value = R.string.invalid_code
            else
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleId = R.string.bravo_text,
                        contentIdOrString = R.string.account_created_successfully,
                        imageId = R.drawable.ic_profile_check,
                        timerCount = COUNT_DOWN_TIME_6s,
                        buttonCloseClick = {
                            // TODO add redirect to sign in screen
                        }
                    )
                )
        }
    }

    @Suppress("MagicNumber")
    fun resendConfirmationCodeAndStartTimer() {
        // TODO add api call and replace mock behavior
        performApiCall {
            clearInvalidCodeError()
            clearVerificationCode()
            delay(200)
            initResendCode()
        }
    }

    sealed class Command {
        object GetSupport : Command()
    }
}
