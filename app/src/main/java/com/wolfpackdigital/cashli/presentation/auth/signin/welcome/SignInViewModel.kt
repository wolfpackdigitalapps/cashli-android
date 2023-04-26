package com.wolfpackdigital.cashli.presentation.auth.signin.welcome

import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.NavigationDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.SignInRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UserSignInRequest
import com.wolfpackdigital.cashli.domain.usecases.SignInUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateSignInFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.AlphaAnimationConfig
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.REPEAT_ANIM_ONE_TIME
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

const val API_ERROR = "Invalid credentials"

class SignInViewModel(
    private val validateSignInFormUseCase: ValidateSignInFormUseCase,
    private val signInUserUseCase: SignInUserUseCase
) : BaseViewModel(), PersistenceService {

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

    fun signUserIn() {
        validateFields {
            val request = createUserSignInRequest()
            request ?: return@validateFields
            val result = signInUserUseCase(request)
            result.onSuccess { newProfile ->
                userProfile = newProfile
                _baseCmd.value = BaseCommand.PerformNavAction(
                    NavigationDirections.actionGlobalHomeGraph(),
                    popUpTo = R.id.navigation,
                    inclusive = true
                )
            }
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                if (error == API_ERROR) {
                    _error.value = R.string.incorrect_credentials_with_email
                } else {
                    _baseCmd.value = BaseCommand.ShowToast(error)
                }
            }
        }
    }

    private fun createUserSignInRequest() =
        if (isEmailCredentialsInUse.value == true) {
            safeLet(email.value, password.value) { email, password ->
                return@safeLet SignInRequest(
                    userSignInRequest = UserSignInRequest(
                        identifier = email,
                        password = password
                    )
                )
            }
        } else {
            safeLet(phoneNumber.value, password.value) { phoneNumber, password ->
                val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                    Constants.PHONE_PREFIX_RO
                else
                    Constants.PHONE_PREFIX_US
                return@safeLet SignInRequest(
                    userSignInRequest = UserSignInRequest(
                        identifier = "$identifierPrefix$phoneNumber",
                        password = password
                    )
                )
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
