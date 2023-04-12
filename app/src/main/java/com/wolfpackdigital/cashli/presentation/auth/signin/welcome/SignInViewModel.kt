package com.wolfpackdigital.cashli.presentation.auth.signin.welcome

import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateSignInFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants.REPEAT_ANIM_ONE_TIME
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.delay

class SignInViewModel(
    private val validateSignInFormUseCase: ValidateSignInFormUseCase
) : BaseViewModel() {

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
    val password = MutableLiveData<String?>()
    val phoneNumber = MutableLiveData<String>()

    val isPasswordVisible = MutableLiveData(false)

    private val _error = MutableLiveData<Int?>()
    val error: LiveData<Int?> = _error

    private fun validateFields(onValidInput: suspend () -> Unit) {
        isEmailCredentialsInUse.value?.let { emailInUse ->
            val signInFormValidationResult = validateSignInFormUseCase(
                email = email.value,
                password = password.value,
                phoneNumber = phoneNumber.value,
                emailInUse
            )
            if (!signInFormValidationResult.successful)
                _error.value = signInFormValidationResult.errorMessageId
            else
                performApiCall { onValidInput() }
        }
    }

    fun onPasswordVisibilityChange() {
        isPasswordVisible.value = isPasswordVisible.value?.not() ?: false
    }

    @Suppress("MagicNumber")
    fun signUserIn() {
        validateFields {
            // TODO add call to BE and nav to success dialog
            delay(2000)
        }
    }

    fun forgotPassword() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            SignInFragmentDirections.actionSignInFragmentToRequestCodeFragment()
        )
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
