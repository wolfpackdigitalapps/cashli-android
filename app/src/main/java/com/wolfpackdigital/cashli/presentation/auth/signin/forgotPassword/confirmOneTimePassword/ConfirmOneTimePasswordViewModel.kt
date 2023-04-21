package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.confirmOneTimePassword

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseValidateCodeViewModel
import kotlinx.coroutines.delay

class ConfirmOneTimePasswordViewModel(
    val phoneNumberOrEmail: String
) : BaseValidateCodeViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.confirm_one_time_password,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    init {
        initResendCode()
    }

    @Suppress("MaxLineLength", "MagicNumber")
    fun onContinueClicked() {
        // TODO Add api call and remove mock data
        performApiCall {
            delay(200)
            _baseCmd.value = BaseCommand.PerformNavAction(
                ConfirmOneTimePasswordFragmentDirections.actionConfirmOneTimePasswordFragmentToResetPasswordFragment()
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

    fun onResendConfirmationCodeClicked() {
        resendConfirmationCodeAndStartTimer()
    }
}
