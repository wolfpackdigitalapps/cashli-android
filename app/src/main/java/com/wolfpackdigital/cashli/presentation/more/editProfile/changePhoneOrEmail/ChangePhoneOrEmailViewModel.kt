package com.wolfpackdigital.cashli.presentation.more.editProfile.changePhoneOrEmail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.EditPhoneOrEmail
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.usecases.SubmitChangeIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidateEmailUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.presentation.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.shared.base.ApiError
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.CONFIRM_ONE_TIME_PASSWORD_DL

class ChangePhoneOrEmailViewModel(
    val editPhoneOrEmail: EditPhoneOrEmail,
    private val validatePhoneNumberFormUseCase: ValidatePhoneNumberFormUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val submitChangeIdentifiersUseCase: SubmitChangeIdentifiersUseCase
) : BaseViewModel() {

    private val _toolbar = MutableLiveData(
        Toolbar(
            titleId = when (editPhoneOrEmail) {
                EditPhoneOrEmail.PHONE -> R.string.change_phone
                EditPhoneOrEmail.EMAIL -> R.string.change_email
            },
            onBack = ::back
        )
    )
    val toolbar: LiveData<Toolbar> = _toolbar

    val phoneOrEmail = MutableLiveData<String>()

    val disableButton = when (editPhoneOrEmail) {
        EditPhoneOrEmail.PHONE -> phoneOrEmail.map { number ->
            number.length > Constants.PHONE_NUMBER_LENGTH
        }

        EditPhoneOrEmail.EMAIL -> MutableLiveData(false)
    }

    private val tooLong = disableButton.map { disabled ->
        if (disabled) R.string.phone_number_length_error else null
    }

    private val onSavePhoneNumberError = MutableLiveData<Any?>(null)

    val phoneNumberError: MediatorLiveData<Any?> = MediatorLiveData<Any?>().apply {
        addSource(tooLong) { error -> value = error }
        addSource(onSavePhoneNumberError) { error -> value = error }
    }

    private val _emailError = MutableLiveData<Any?>()
    val emailError: LiveData<Any?> = _emailError

    fun onSaveClicked() {
        when (editPhoneOrEmail) {
            EditPhoneOrEmail.PHONE -> handlePhoneChange()
            EditPhoneOrEmail.EMAIL -> handleEmailChange()
        }
    }

    private fun handleEmailChange() {
        if (validateEmail() == true) {
            performApiCall {
                val request = IdentifiersRequest(
                    channel = IdentifierChannel.EMAIL,
                    identifier = phoneOrEmail.value.orEmpty()
                )
                val result = submitChangeIdentifiersUseCase(request)
                result.onSuccess {
                    _baseCmd.value = BaseCommand.PerformNavDeepLink(
                        deepLink = "$CONFIRM_ONE_TIME_PASSWORD_DL${
                        request.identifier}/${CodeReceivedViaType.EMAIL.ordinal}/${true}"
                    )
                }
                result.onError {
                    showApiError(it)
                }
            }
        }
    }

    private fun handlePhoneChange() {
        phoneOrEmail.value?.let { phoneNumber ->
            val validatePhoneNumberResult = validatePhoneNumberFormUseCase(phoneNumber)
            if (!validatePhoneNumberResult.successful) {
                onSavePhoneNumberError.value = validatePhoneNumberResult.errorMessageId
            } else {
                val identifierPrefix = if (BuildConfig.FLAVOR == Constants.VARIANT_DEVELOP)
                    Constants.PHONE_PREFIX_RO
                else
                    Constants.PHONE_PREFIX_US
                performApiCall {
                    val request = IdentifiersRequest(
                        channel = IdentifierChannel.SMS,
                        identifier = "$identifierPrefix$phoneNumber"
                    )
                    val result = submitChangeIdentifiersUseCase(request)
                    result.onSuccess {
                        _baseCmd.value = BaseCommand.PerformNavDeepLink(
                            deepLink = "$CONFIRM_ONE_TIME_PASSWORD_DL$phoneNumber/${
                            CodeReceivedViaType.SMS.ordinal
                            }/${true}"
                        )
                    }
                    result.onError {
                        showApiError(it)
                    }
                }
            }
        }
    }

    private fun showApiError(apiError: ApiError) {
        val error =
            apiError.errors?.firstOrNull() ?: apiError.messageId ?: R.string.generic_error
        when (apiError.errorCode) {
            Constants.ERROR_CODE_422, Constants.ERROR_CODE_429 ->
                phoneNumberError.value = error

            else -> _baseCmd.value = BaseCommand.ShowToast(error)
        }
    }

    private fun validateEmail() = phoneOrEmail.value?.let { email ->
        if (validateEmailUseCase(email)) {
            true
        } else {
            _emailError.value = R.string.email_error
            false
        }
    }

    fun clearPhoneOrEmailError() {
        _emailError.value = null
        onSavePhoneNumberError.value = null
    }
}
