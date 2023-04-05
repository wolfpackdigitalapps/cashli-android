@file:Suppress("TooManyFunctions", "ComplexCondition")

package com.wolfpackdigital.cashli.presentation.auth.signup.createProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.containOnlyDigits
import com.wolfpackdigital.cashli.shared.utils.extensions.containOnlyLettersAndComma
import com.wolfpackdigital.cashli.shared.utils.extensions.hasEmailPattern
import com.wolfpackdigital.cashli.shared.utils.extensions.hasNamePattern

class CreateProfileViewModel : BaseViewModel() {

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

    val isEnabled = MediatorLiveData<Boolean>().apply {
        addSource(firstName) { value = enableButton() }
        addSource(lastName) { value = enableButton() }
        addSource(street) { value = enableButton() }
        addSource(zipCode) { value = enableButton() }
        addSource(cityAndState) { value = enableButton() }
        addSource(email) { value = enableButton() }
    }

    private val _zipCodeError = MutableLiveData<Int?>(null)
    val zipCodeError: LiveData<Int?> = _zipCodeError

    private val _emailError = MutableLiveData<Int?>(null)
    val emailError: LiveData<Int?> = _emailError

    private val _cityAndStateError = MutableLiveData<Int?>(null)
    val cityAndStateError: LiveData<Int?> = _cityAndStateError

    private val _streetError = MutableLiveData<Int?>(null)
    val streetError: LiveData<Int?> = _streetError

    private val _firstNameError = MutableLiveData<Int?>(null)
    val firstNameError: LiveData<Int?> = _firstNameError

    private val _lastNameError = MutableLiveData<Int?>(null)
    val lastNameError: LiveData<Int?> = _lastNameError

    private fun enableButton() =
        firstName.value?.isNotEmpty() == true && lastName.value?.isNotEmpty() == true &&
            street.value?.isNotEmpty() == true && email.value?.isNotEmpty() == true &&
            zipCode.value?.isNotEmpty() == true && cityAndState.value?.isNotEmpty() == true

    fun onContinueClicked() {
        if (validateZipCode() == true && validateEmail() == true && validateCityAndState() == true &&
            validateStreet() == true && validateFirstName() == true && validateLastName() == true
        ) {
            _baseCmd.value = BaseCommand.PerformNavAction(
                CreateProfileFragmentDirections.actionCreateProfileFragmentToValidateCodeFragment(
                    CodeReceivedViaType.EMAIL
                )
            )
        }
    }

    private fun validateZipCode() = zipCode.value?.let { code ->
        if (code.containOnlyDigits() && code.length == Constants.ZIP_CODE_LENGTH) {
            true
        } else {
            _zipCodeError.value = R.string.zip_code_error
            false
        }
    }

    private fun validateEmail() = email.value?.let { email ->
        if (email.hasEmailPattern()) {
            true
        } else {
            _emailError.value = R.string.email_error
            false
        }
    }

    private fun validateCityAndState() = cityAndState.value?.let { input ->
        if (input.containOnlyLettersAndComma()) {
            true
        } else {
            _cityAndStateError.value = R.string.city_and_state_error
            false
        }
    }

    private fun validateStreet() = street.value?.let { street ->
        if (street.length >= Constants.MIN_CHARS_2) {
            true
        } else {
            _streetError.value = R.string.street_error
            false
        }
    }

    private fun validateFirstName() = firstName.value?.let { name ->
        if (name.hasNamePattern() && name.length >= Constants.MIN_CHARS_2) {
            true
        } else {
            _firstNameError.value = R.string.name_error
            false
        }
    }

    private fun validateLastName() = lastName.value?.let { name ->
        if (name.hasNamePattern() && name.length >= Constants.MIN_CHARS_2) {
            true
        } else {
            _lastNameError.value = R.string.name_error
            false
        }
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
}
