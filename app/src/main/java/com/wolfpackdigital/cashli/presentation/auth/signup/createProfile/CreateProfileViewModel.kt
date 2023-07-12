@file:Suppress("TooManyFunctions", "ComplexCondition")

package com.wolfpackdigital.cashli.presentation.auth.signup.createProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.asLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.enums.Language
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.entities.requests.UserProfileRequest
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateCityAndStateFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEmailUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateFirstNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateLastNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateStreetFieldUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateZipCodeUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.COMMA
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_429
import com.wolfpackdigital.cashli.shared.utils.LiveEvent
import kotlinx.coroutines.flow.combine

@Suppress("LongParameterList")
class CreateProfileViewModel(
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validateStreetFieldUseCase: ValidateStreetFieldUseCase,
    private val validateZipCodeUseCase: ValidateZipCodeUseCase,
    private val validateFirstNameFormUseCase: ValidateFirstNameFormUseCase,
    private val validateLastNameFormUseCase: ValidateLastNameFormUseCase,
    private val validateCityAndStateFormUseCase: ValidateCityAndStateFormUseCase,
    private val submitRegistrationIdentifiersUseCase: SubmitRegistrationIdentifiersUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = Constants.STEP_2,
            isStepCounterVisible = true,
            isBackVisible = false
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val firstName = MutableLiveData<String>()
    val lastName = MutableLiveData<String>()
    val street = MutableLiveData<String>()
    val email = MutableLiveData<String>()
    val zipCode = MutableLiveData<String>()
    val cityAndState = MutableLiveData<String>()

    val isEnabled = combine(
        firstName.asFlow(), lastName.asFlow(), street.asFlow(),
        zipCode.asFlow(), email.asFlow(), cityAndState.asFlow()
    ) { inputs ->
        inputs.fold(true) { acc, field -> acc && field.isNotEmpty() }
    }.asLiveData()

    private val _zipCodeError = MutableLiveData<Int?>(null)
    val zipCodeError: LiveData<Int?> = _zipCodeError

    private val _emailError = MutableLiveData<Any?>(null)
    val emailError: LiveData<Any?> = _emailError

    private val _cityAndStateError = MutableLiveData<Int?>(null)
    val cityAndStateError: LiveData<Int?> = _cityAndStateError

    private val _streetError = MutableLiveData<Int?>(null)
    val streetError: LiveData<Int?> = _streetError

    private val _firstNameError = MutableLiveData<Int?>(null)
    val firstNameError: LiveData<Int?> = _firstNameError

    private val _lastNameError = MutableLiveData<Int?>(null)
    val lastNameError: LiveData<Int?> = _lastNameError

    fun onContinueClicked() {
        if ((validateZipCode() == true) and (validateEmail() == true) and (validateCityAndState() == true) and
            (validateStreet() == true) and (validateFirstName() == true) and (validateLastName() == true)
        ) {
            performApiCall {
                val request = IdentifiersRequest(
                    channel = IdentifierChannel.EMAIL,
                    identifier = email.value.orEmpty(),
                    locale = language ?: Language.ENGLISH
                )
                val result = submitRegistrationIdentifiersUseCase(request)
                result.onSuccess {
                    val cityAndStateArray = cityAndState.value?.split(COMMA)?.map {it.trim()}
                    val temporaryUserProfileRequest = UserProfileRequest(
                        firstName = firstName.value.orEmpty(),
                        lastName = lastName.value.orEmpty(),
                        email = email.value.orEmpty(),
                        street = street.value.orEmpty(),
                        zipCode = zipCode.value.orEmpty(),
                        city = cityAndStateArray?.firstOrNull().orEmpty(),
                        state = cityAndStateArray?.lastOrNull().orEmpty(),
                        language = language ?: Language.ENGLISH
                    )
                    _cmd.value = Command.SaveUserProfile(temporaryUserProfileRequest)
                    _baseCmd.value = BaseCommand.PerformNavAction(
                        CreateProfileFragmentDirections.actionCreateProfileFragmentToValidateCodeFragment(
                            CodeReceivedViaType.EMAIL
                        )
                    )
                }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    when (it.errorCode) {
                        Constants.ERROR_CODE_422, ERROR_CODE_429 -> _emailError.value = error
                        else -> _baseCmd.value = BaseCommand.ShowToast(error)
                    }
                }
            }
        }
    }

    private fun validateZipCode() = zipCode.value?.let { code ->
        if (validateZipCodeUseCase(code)) {
            true
        } else {
            _zipCodeError.value = R.string.zip_code_error
            false
        }
    }

    private fun validateEmail() = email.value?.let { email ->
        if (validateEmailUseCase(email)) {
            true
        } else {
            _emailError.value = R.string.email_error
            false
        }
    }

    private fun validateCityAndState() = cityAndState.value?.let { input ->
        val validateCityStateResult = validateCityAndStateFormUseCase(input)
        if (!validateCityStateResult.successful) {
            _cityAndStateError.value = validateCityStateResult.errorMessageId
            return@let false
        }
        return@let true
    }

    private fun validateStreet() = street.value?.let { street ->
        if (validateStreetFieldUseCase(street)) {
            true
        } else {
            _streetError.value = R.string.street_error
            false
        }
    }

    private fun validateFirstName() = firstName.value?.let { name ->
        val validateFirstNameResult = validateFirstNameFormUseCase(name)
        if (!validateFirstNameResult.successful) {
            _firstNameError.value = validateFirstNameResult.errorMessageId
            return@let false
        }
        return@let true
    }

    private fun validateLastName() = lastName.value?.let { name ->
        val validateLastNameResult = validateLastNameFormUseCase(name)
        if (!validateLastNameResult.successful) {
            _lastNameError.value = validateLastNameResult.errorMessageId
            return@let false
        }
        return@let true
    }

    fun clearZipCodeError() {
        _zipCodeError.value = null
    }

    fun clearEmailError() {
        _emailError.value = null
    }

    fun clearCityAndStateError() {
        _cityAndStateError.value = null
    }

    fun clearStreetError() {
        _streetError.value = null
    }

    fun clearFirstNameError() {
        _firstNameError.value = null
    }

    fun clearLastNameError() {
        _lastNameError.value = null
    }

    sealed class Command {
        data class SaveUserProfile(val temporaryUserProfileRequest: UserProfileRequest) : Command()
    }

    companion object {
        const val ZIP_CODE_LENGTH = 5
    }
}
