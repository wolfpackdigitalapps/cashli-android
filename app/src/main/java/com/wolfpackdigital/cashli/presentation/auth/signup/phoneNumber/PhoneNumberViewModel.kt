package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.extensions.hasPhoneNumberPattern

class PhoneNumberViewModel : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleLogoId = R.drawable.ic_logo_toolbar,
            currentStep = Constants.STEP_1,
            isStepCounterVisible = true,
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val phoneNumber = MutableLiveData<String>()

    val disableButton = phoneNumber.map { number ->
        number.length > Constants.PHONE_NUMBER_LENGTH
    }

    private val tooLong = disableButton.map { disabled ->
        if (disabled) R.string.phone_number_length_error else null
    }

    private val _onContinueError = MutableLiveData<Int?>(null)
    var onContinueError: LiveData<Int?> = _onContinueError

    val error: MediatorLiveData<Int?> = MediatorLiveData<Int?>().apply {
        addSource(tooLong) { error -> value = error }
        addSource(onContinueError) { error -> value = error }
    }

    fun onContinueClicked() {
        phoneNumber.value?.let { number ->
            if (number.hasPhoneNumberPattern()) {
                if (number.length == Constants.PHONE_NUMBER_LENGTH) {
                    // TODO navigate to next screen
                } else {
                    _onContinueError.value = R.string.phone_number_length_error
                }
            } else {
                _onContinueError.value = R.string.phone_number_digits_error
            }
        }
    }
}
