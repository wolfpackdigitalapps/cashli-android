package com.wolfpackdigital.cashli.presentation.auth.signin.forgotPassword.requestCode

import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.usecases.SubmitPasswordIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateRequestCodeFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class RequestCodeViewModel(
    private val validateRequestCodeFormUseCase: ValidateRequestCodeFormUseCase,
    private val submitPasswordIdentifiersUseCase: SubmitPasswordIdentifiersUseCase
) : BaseViewModel(), PersistenceService {

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
    private val _error = MutableLiveData<Any?>(null)
    val error: LiveData<Any?> = _error

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

    private fun validateFields(onValidInput: suspend (Boolean) -> Unit) {
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
                    onValidInput(emailInUse)
                }
            }
        }
    }

    private fun navigateToConfirmOneTimePasswordScreen(isEmailInUse: Boolean) {
        _baseCmd.value = isEmailResetInUse.value?.let { emailInUse ->
            when (emailInUse) {
                true -> email.value
                false -> phoneNumber.value
            }
        }?.let {
            RequestCodeFragmentDirections.actionRequestCodeFragmentToConfirmOneTimePasswordFragment(
                it,
                if (isEmailInUse)
                    CodeReceivedViaType.EMAIL.ordinal
                else
                    CodeReceivedViaType.SMS.ordinal,
                false
            )
        }?.let {
            BaseCommand.PerformNavAction(
                it
            )
        }
    }

    fun onContinueClicked() {
        safeLet(email.value, phoneNumber.value) { email, number ->
            validateFields { emailInUse ->
                val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                    Constants.PHONE_PREFIX_RO
                else
                    Constants.PHONE_PREFIX_US
                val request = IdentifiersRequest(
                    channel = if (emailInUse) IdentifierChannel.EMAIL else IdentifierChannel.SMS,
                    identifier = if (emailInUse) email else "$identifierPrefix$number",
                    locale = language ?: Language.ENGLISH
                )
                val result = submitPasswordIdentifiersUseCase(request)
                result.onSuccess {
                    navigateToConfirmOneTimePasswordScreen(emailInUse)
                }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    when (it.errorCode) {
                        Constants.ERROR_CODE_422, Constants.ERROR_CODE_429 -> _error.value = error
                        else -> _baseCmd.value = BaseCommand.ShowToast(error)
                    }
                }
            }
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
