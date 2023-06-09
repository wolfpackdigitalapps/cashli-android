package com.wolfpackdigital.cashli.presentation.auth.signup.choosePassword

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.NavigationDirections
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.requests.CreateUserProfileRequest
import com.wolfpackdigital.cashli.domain.usecases.RegisterDeviceTokenUseCase
import com.wolfpackdigital.cashli.domain.usecases.RegisterNewUserUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateChoosePasswordFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.PopupConfig
import com.wolfpackdigital.cashli.presentation.entities.TextSpanAction
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.STEP_3
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import com.wolfpackdigital.cashli.shared.utils.extensions.safeLet
import kotlinx.coroutines.flow.combine

// Span actions
private const val VALUE_SPAN_OPEN_PP = "openPP"
private const val VALUE_SPAN_OPEN_TS = "openTS"
private const val VALUE_SPAN_OPEN_DPP = "openDPP"
private const val VALUE_SPAN_OPEN_DTS = "openDTS"

class ChoosePasswordViewModel(
    private val validateChoosePasswordFormUseCase: ValidateChoosePasswordFormUseCase,
    private val registerNewUserUseCase: RegisterNewUserUseCase,
    private val registerDeviceTokenUseCase: RegisterDeviceTokenUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = STEP_3,
            isStepCounterVisible = true,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar
    val termsAndConditionsSpanActions: List<TextSpanAction> = listOf(
        TextSpanAction(
            actionKey = VALUE_SPAN_OPEN_TS,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.terms_and_conditions_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = VALUE_SPAN_OPEN_PP,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.privacy_policy_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = VALUE_SPAN_OPEN_DTS,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.dwolla_terms_of_service_url)
            },
            spanTextColor = R.color.colorWhiteF5
        ),
        TextSpanAction(
            actionKey = VALUE_SPAN_OPEN_DPP,
            action = {
                _baseCmd.value = BaseCommand.OpenUrl(R.string.dwolla_privacy_policy_url)
            },
            spanTextColor = R.color.colorWhiteF5
        )
    )

    @StringRes
    val termsAndConditionsTextId: Int = R.string.terms_and_conditions
    val termsAccepted = MutableLiveData(false)

    val password = MutableLiveData<String?>()
    val confirmPassword = MutableLiveData<String?>()

    val isPasswordVisible = MutableLiveData(false)
    val isConfirmPasswordVisible = MutableLiveData(false)

    private val _passwordError = MutableLiveData<Int?>()
    val passwordError: LiveData<Int?> = _passwordError

    val isFinishSignUpEnabled =
        combine(
            termsAccepted.asFlow(),
            password.asFlow(),
            confirmPassword.asFlow()
        ) { termsAccepted, password, confirmPassword ->
            termsAccepted && !password.isNullOrBlank() && !confirmPassword.isNullOrBlank()
        }.asLiveData()

    fun onPasswordVisibilityChange() {
        isPasswordVisible.value = isPasswordVisible.value?.not() ?: false
    }

    fun onConfirmPasswordVisibilityChange() {
        isConfirmPasswordVisible.value = isConfirmPasswordVisible.value?.not() ?: false
    }

    private fun validatePasswords(onValidInput: suspend (String) -> Unit) {
        safeLet(password.value, confirmPassword.value) { password, confirmPassword ->
            val choosePasswordFormValidationResult = validateChoosePasswordFormUseCase(
                password = password,
                confirmPassword = confirmPassword
            )
            if (!choosePasswordFormValidationResult.successful)
                _passwordError.value = choosePasswordFormValidationResult.errorMessageId
            else
                performApiCall { onValidInput(password) }
        }
    }

    fun onFinishSignUpClicked() {
        validatePasswords { password ->
            _cmd.value = Command.SavePassword(password)
            _cmd.value = Command.CreateUserProfileRequest
        }
    }

    fun registerNewUser(profileRequest: CreateUserProfileRequest?) {
        profileRequest ?: return
        performApiCall {
            val result = registerNewUserUseCase(profileRequest)
            result.onSuccess { newUserProfile ->
                userProfile = newUserProfile
                token = newUserProfile.tokens
                registerDeviceTokenUseCase(Unit)
                _cmd.value = Command.ClearSignUpData
                _baseCmd.value = BaseCommand.ShowPopupById(
                    PopupConfig(
                        titleIdOrString = R.string.bravo_text,
                        contentIdOrString = R.string.account_created_successfully,
                        imageId = R.drawable.ic_profile_check,
                        timerCount = Constants.COUNT_DOWN_TIME_6_SEC,
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
            result.onError {
                val error =
                    it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                _baseCmd.value = BaseCommand.ShowToast(error)
            }
        }
    }

    fun clearFieldsError() {
        _passwordError.value = null
    }

    sealed class Command {
        data class SavePassword(val password: String) : Command()
        object CreateUserProfileRequest : Command()
        object ClearSignUpData : Command()
    }
}
