package com.wolfpackdigital.cashli.presentation.more.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.EditPhoneOrEmail
import com.wolfpackdigital.cashli.domain.entities.requests.UpdateUserProfileRequest
import com.wolfpackdigital.cashli.domain.usecases.UpdateUserProfileUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateFirstNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateLastNameFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.EMPTY_STRING

@Suppress("TooManyFunctions")
class EditProfileViewModel(
    private val validateFirstNameFormUseCase: ValidateFirstNameFormUseCase,
    private val validateLastNameFormUseCase: ValidateLastNameFormUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = R.string.edit_profile,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    private val _firstNameLabel = MutableLiveData(userProfile?.firstName)
    val firstNameLabel: LiveData<String?> = _firstNameLabel

    private val _lastNameLabel = MutableLiveData(userProfile?.lastName)
    val lastNameLabel: LiveData<String?> = _lastNameLabel

    private val _phoneNumber = MutableLiveData(userProfile?.phoneNumber)
    val phoneNumber: LiveData<String?> = _phoneNumber

    private val _email = MutableLiveData(userProfile?.email)
    val email: LiveData<String?> = _email

    val newFirstName = MutableLiveData<String>()
    val newLastName = MutableLiveData<String>()

    private val _isFirstNameEnabled = MutableLiveData(false)
    val isFirstNameEnabled: LiveData<Boolean> = _isFirstNameEnabled

    private val _isLastNameEnabled = MutableLiveData(false)
    val isLastNameEnabled: LiveData<Boolean> = _isLastNameEnabled

    private val _firstNameError = MutableLiveData<Any?>(null)
    val firstNameError: LiveData<Any?> = _firstNameError

    private val _lastNameError = MutableLiveData<Any?>(null)
    val lastNameError: LiveData<Any?> = _lastNameError

    fun setUpdatedData() {
        _phoneNumber.value = userProfile?.phoneNumber
        _email.value = userProfile?.email
        _firstNameLabel.value = userProfile?.firstName
        _lastNameLabel.value = userProfile?.lastName
    }

    fun toggleFirstNameEnabled() {
        _isFirstNameEnabled.value = isFirstNameEnabled.value?.not()
    }

    fun toggleLastNameEnabled() {
        _isLastNameEnabled.value = isLastNameEnabled.value?.not()
    }

    fun onChangeEmailClicked() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            EditProfileFragmentDirections.actionEditProfileFragmentToChangePhoneOrEmailFragment(
                EditPhoneOrEmail.EMAIL
            )
        )
    }

    fun onChangePhoneClicked() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            EditProfileFragmentDirections.actionEditProfileFragmentToChangePhoneOrEmailFragment(
                EditPhoneOrEmail.PHONE
            )
        )
    }

    fun onChangePasswordClicked() {
        _baseCmd.value = BaseCommand.PerformNavAction(
            EditProfileFragmentDirections.actionEditProfileFragmentToChangePasswordFragment()
        )
    }

    fun onSaveClicked() {
        if (((validateFirstName() == true) and (validateLastName() == true))
            or (validateFirstName() == true) or (validateLastName() == true)
        ) {
            performApiCall {
                val request = UpdateUserProfileRequest(
                    firstName = userProfile?.firstName.orEmpty(),
                    lastName = userProfile?.lastName.orEmpty(),
                    street = userProfile?.street.orEmpty(),
                    zipCode = userProfile?.zipCode.orEmpty(),
                    city = userProfile?.city.orEmpty(),
                    state = userProfile?.state.orEmpty()
                )
                val result = updateUserProfileUseCase(request)
                result.onSuccess {
                    userProfile = it
                    clearData()
                    setUpdatedData()
                }
                result.onError {
                    val error =
                        it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                    when (it.errorCode) {
                        Constants.ERROR_CODE_422 -> _firstNameError.value = error
                        else -> _baseCmd.value = BaseCommand.ShowToast(error)
                    }
                }
            }
        }
    }

    private fun validateFirstName() = newFirstName.value?.let { name ->
        if (name.isNotEmpty()) {
            val validateFirstNameResult = validateFirstNameFormUseCase(name)
            if (!validateFirstNameResult.successful) {
                _firstNameError.value = validateFirstNameResult.errorMessageId
                return@let false
            }
            userProfile = userProfile?.copy(firstName = name)
            return@let true
        } else {
            return@let false
        }
    }

    private fun validateLastName() = newLastName.value?.let { name ->
        if (name.isNotEmpty()) {
            val validateLastNameResult = validateLastNameFormUseCase(name)
            if (!validateLastNameResult.successful) {
                _lastNameError.value = validateLastNameResult.errorMessageId
                return@let false
            }
            userProfile = userProfile?.copy(lastName = name)
            return@let true
        } else {
            return@let false
        }
    }

    fun clearFirstNameError() {
        _firstNameError.value = null
    }

    fun clearLastNameError() {
        _lastNameError.value = null
    }

    private fun clearData() {
        newFirstName.value = EMPTY_STRING
        newLastName.value = EMPTY_STRING
    }
}
