package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode

import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateRequestCodeFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import kotlinx.coroutines.delay

private const val PHONE_NUMBER_PREFIX = "+1 "

class RequestCodeViewModel(
    private val validateRequestCodeFormUseCase: ValidateRequestCodeFormUseCase
) : BaseViewModel() {

    private val _cmd = MutableLiveData<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.forgot_password,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val phoneNumber = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    private val _error = MutableLiveData<Int?>()
    val error: LiveData<Int?> = _error

    private val _isEmailResetInUse = MutableLiveData(true)
    val isEmailResetInUse: LiveData<Boolean> = _isEmailResetInUse

    @StringRes
    private val _credentialsInUseTextId = MutableLiveData<Int>()
    val credentialsInUseTextId: LiveData<Int> = _credentialsInUseTextId

    @StringRes
    private val _usePhoneOrEmailTextId = MutableLiveData<Int>()
    val usePhoneOrEmailTextId: LiveData<Int> = _usePhoneOrEmailTextId

    private val alphaAnimationConfig by lazy {
        AlphaAnimationConfig(
            repeatCountValue = Constants.REPEAT_ANIM_ONE_TIME,
            repeatModeValue = Animation.REVERSE,
            actionOnRepeat = {
                if (isEmailResetInUse.value == false) {
                    _credentialsInUseTextId.value = R.string.use_your_email
                    _usePhoneOrEmailTextId.value = R.string.forgot_password_screen_message_phone
                } else {
                    _credentialsInUseTextId.value = R.string.use_your_phone
                    _usePhoneOrEmailTextId.value = R.string.forgot_password_screen_message_email
                }
            }
        )
    }

    val togglePhoneEmailCredentialsText = _isEmailResetInUse.map {
        _cmd.value = Command.ToggleTextVisibilityAnimated(alphaAnimationConfig)
        clearFieldsError()
        closeKeyboardAndClearFocus()
    }
    fun togglePhoneEmailCredentials() {
        _isEmailResetInUse.value = _isEmailResetInUse.value?.not()
        isEmailResetInUse.value?.let { inUse ->
            when (inUse) {
                true -> {
                    _credentialsInUseTextId.value = R.string.use_your_phone
                    _usePhoneOrEmailTextId.value = R.string.forgot_password_screen_message_email
                }
                false -> {
                    _credentialsInUseTextId.value = R.string.use_your_email
                    _usePhoneOrEmailTextId.value = R.string.forgot_password_screen_message_phone
                }
            }
        }
    }

    private fun validateFields(onValidInput: suspend () -> Unit) {
        isEmailResetInUse.value?.let { emailInUse ->
            val phoneOrEmailValidationResult = validateRequestCodeFormUseCase(
                email = email.value,
                phoneNumber = phoneNumber.value,
                emailInUse
            )
            if (!phoneOrEmailValidationResult.successful) {
                _error.value = phoneOrEmailValidationResult.errorMessageId
            } else {
                performApiCall {
                    onValidInput()
                }
            }
        }
    }

    private fun navigateToConfirmOneTimePasswordScreen() {
        _baseCmd.value = isEmailResetInUse.value?.let { emailInUse ->
            when (emailInUse) {
                true -> email.value
                false -> PHONE_NUMBER_PREFIX + phoneNumber.value
            }
        }?.let {
            RequestCodeFragmentDirections.actionRequestCodeFragmentToConfirmOneTimePasswordFragment(
                it
            )
        }?.let {
            BaseCommand.PerformNavAction(
                it
            )
        }
    }
    @Suppress("MagicNumber")

    fun onContinueClicked() {
        validateFields {
            delay(200)
            navigateToConfirmOneTimePasswordScreen()
        }
    }

    fun clearFieldsError() {
        _error.value = null
    }

    sealed class Command {
        data class ToggleTextVisibilityAnimated(
            val alphaAnimationConfig: AlphaAnimationConfig
        ) : Command()
    }
}
