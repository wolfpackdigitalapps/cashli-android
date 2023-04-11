package com.wolfpackdigital.cashli.presentation.auth.signin.welcome

import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BasePasswordValidatorViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.PHONE_NUMBER_LENGTH
import com.wolfpackdigital.cashli.shared.utils.Constants.REPEAT_ANIM_ONE_TIME
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.containOnlyDigits
import com.wolfpackdigital.cashli.shared.utils.extensions.hasEmailPattern
import com.wolfpackdigital.cashli.shared.utils.extensions.hasPasswordPattern
import kotlinx.coroutines.delay

class SignInViewModel : BasePasswordValidatorViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command>
        get() = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.welcome_back,
            onBack = ::onReturnToOnboarding
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private val alphaAnimationConfig by lazy {
        AlphaAnimationConfig(
            repeatCountValue = REPEAT_ANIM_ONE_TIME,
            repeatModeValue = Animation.REVERSE,
            actionOnRepeat = {
                if (isEmailCredentialsInUse.value == false)
                    _credentialsInUseTextId.value = R.string.use_your_email
                else
                    _credentialsInUseTextId.value = R.string.use_your_phone
            }
        )
    }

    @StringRes
    private val _credentialsInUseTextId = MutableLiveData<Int>()
    val credentialsInUseTextId: LiveData<Int> = _credentialsInUseTextId

    private val _isEmailCredentialsInUse = MutableLiveData(false)
    val isEmailCredentialsInUse: LiveData<Boolean> = _isEmailCredentialsInUse

    val togglePhoneEmailCredentialsText = isEmailCredentialsInUse.map {
        _cmd.value = Command.ToggleTextVisibilityAnimated(alphaAnimationConfig)
        clearFieldsError()
        closeKeyboardAndClearFocus()
    }

    val email = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()

    private val _error = MutableLiveData<Int?>()
    val error: LiveData<Int?> = _error

    private fun validateFields(onValidInput: suspend () -> Unit) {
        isEmailCredentialsInUse.value?.let { emailInUse ->
            var error = false
            _error.value = when {
                (emailInUse && email.value.isNullOrBlank()) ||
                    (!emailInUse && phoneNumber.value.isNullOrBlank()) -> {
                    error = true
                    R.string.email_or_phone_can_not_be_empty
                }
                password.value.isNullOrBlank() -> {
                    error = true
                    R.string.password_can_not_be_empty
                }
                emailInUse -> {
                    if (!validateEmail() || !validatePassword()) {
                        error = true
                        R.string.incorrect_credentials_with_email
                    } else {
                        null
                    }
                }
                !validatePhoneNumber() || !validatePassword() -> {
                    error = true
                    R.string.incorrect_credentials_with_phone
                }
                else -> null
            }
            if (!error)
                performApiCall { onValidInput() }
        }
    }

    private fun validatePassword() = password.value?.let { password ->
        when {
            password.length < Constants.PASSWORD_MIN_LENGTH || !password.hasPasswordPattern() -> false
            else -> true
        }
    } ?: false

    private fun validateEmail() = email.value?.let { email ->
        when {
            !email.hasEmailPattern() -> false
            else -> true
        }
    } ?: false

    private fun validatePhoneNumber() = phoneNumber.value?.let { phoneNumber ->
        when {
            !phoneNumber.containOnlyDigits() || phoneNumber.length != PHONE_NUMBER_LENGTH -> false
            else -> true
        }
    } ?: false

    @Suppress("MagicNumber")
    fun signUserIn() {
        validateFields {
            // TODO add call to BE and nav to success dialog
            delay(2000)
        }
    }

    fun forgotPassword() {
        // TODO go to next screen
    }

    fun goToSignUp() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            SignInFragmentDirections.actionSignInFragmentToInformativeFragment(),
            popUpTo = R.id.onboardingFragment
        )
    }

    fun onReturnToOnboarding() {
        _cmd.value = Command.ReturnToOnboarding
        back()
    }

    fun togglePhoneEmailCredentials() {
        _isEmailCredentialsInUse.value = isEmailCredentialsInUse.value?.not()
    }

    fun clearFieldsError() {
        _error.value = null
    }

    sealed class Command {
        object ReturnToOnboarding : Command()
        data class ToggleTextVisibilityAnimated(
            val alphaAnimationConfig: AlphaAnimationConfig
        ) : Command()
    }
}
