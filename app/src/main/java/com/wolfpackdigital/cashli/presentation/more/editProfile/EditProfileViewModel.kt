package com.wolfpackdigital.cashli.presentation.more.editProfile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateFirstNameFormUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateLastNameFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.persistence.PersistenceService

class EditProfileViewModel(
    private val validateFirstNameFormUseCase: ValidateFirstNameFormUseCase,
    private val validateLastNameFormUseCase: ValidateLastNameFormUseCase
) : BaseViewModel(), PersistenceService {

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

    //TODO replace that 3
    private val _phoneNumber = MutableLiveData(userProfile?.phoneNumber?.substring(3))
    val phoneNumber: LiveData<String?> = _phoneNumber

    private val _email = MutableLiveData(userProfile?.email)
    val email: LiveData<String?> = _email

    val newFirstName = MutableLiveData<String>()
    val newLastName = MutableLiveData<String>()

    private val _isFirstNameEnabled = MutableLiveData(false)
    val isFirstNameEnabled: LiveData<Boolean> = _isFirstNameEnabled

    private val _isLastNameEnabled = MutableLiveData(false)
    val isLastNameEnabled: LiveData<Boolean> = _isLastNameEnabled

    private val _firstNameError = MutableLiveData<Int?>(null)
    val firstNameError: LiveData<Int?> = _firstNameError

    private val _lastNameError = MutableLiveData<Int?>(null)
    val lastNameError: LiveData<Int?> = _lastNameError

    fun toggleFirstNameEnabled() {
        _isFirstNameEnabled.value = isFirstNameEnabled.value?.not()
    }

    fun toggleLastNameEnabled() {
        _isLastNameEnabled.value = isLastNameEnabled.value?.not()
    }

    fun onSaveClicked() {
        if ((validateFirstName() == true) and (validateLastName() == true)) {

        }

    }

    private fun validateFirstName() = newFirstName.value?.let { name ->
        val validateFirstNameResult = validateFirstNameFormUseCase(name)
        if (!validateFirstNameResult.successful) {
            _firstNameError.value = validateFirstNameResult.errorMessageId
            return@let false
        }
        return@let true
    }

    private fun validateLastName() = newLastName.value?.let { name ->
        val validateLastNameResult = validateLastNameFormUseCase(name)
        if (!validateLastNameResult.successful) {
            _lastNameError.value = validateLastNameResult.errorMessageId
            return@let false
        }
        return@let true
    }

    fun clearFirstNameError() {
        _firstNameError.value = null
    }

    fun clearLastNameError() {
        _lastNameError.value = null
    }
}