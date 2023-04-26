package com.wolfpackdigital.cashli.presentation.auth.signup.phoneNumber

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.wolfpackdigital.cashli.BuildConfig
import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.enums.CodeReceivedViaType
import com.wolfpackdigital.cashli.domain.entities.enums.IdentifierChannel
import com.wolfpackdigital.cashli.domain.entities.requests.IdentifiersRequest
import com.wolfpackdigital.cashli.domain.usecases.SubmitRegistrationIdentifiersUseCase
import com.wolfpackdigital.cashli.domain.usecases.validations.ValidatePhoneNumberFormUseCase
import com.wolfpackdigital.cashli.presentation.entities.Toolbar
import com.wolfpackdigital.cashli.shared.base.BaseCommand
import com.wolfpackdigital.cashli.shared.base.BaseViewModel
import com.wolfpackdigital.cashli.shared.base.onError
import com.wolfpackdigital.cashli.shared.base.onSuccess
import com.wolfpackdigital.cashli.shared.utils.Constants
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_422
import com.wolfpackdigital.cashli.shared.utils.Constants.ERROR_CODE_429
import com.wolfpackdigital.cashli.shared.utils.Constants.PHONE_PREFIX_RO
import com.wolfpackdigital.cashli.shared.utils.Constants.PHONE_PREFIX_US
import com.wolfpackdigital.cashli.shared.utils.Constants.VARIANT_DEVELOP
import com.wolfpackdigital.cashli.shared.utils.LiveEvent

class PhoneNumberViewModel(
    private val submitRegistrationIdentifiersUseCase: SubmitRegistrationIdentifiersUseCase,
    private val validatePhoneNumberFormUseCase: ValidatePhoneNumberFormUseCase
) : BaseViewModel() {

    private val _cmd = LiveEvent<Command>()
    val cmd: LiveData<Command> = _cmd

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

    private val onContinueError = MutableLiveData<Any?>(null)

    val error: MediatorLiveData<Any?> = MediatorLiveData<Any?>().apply {
        addSource(tooLong) { error -> value = error }
        addSource(onContinueError) { error -> value = error }
    }

    fun onContinueClicked() {
        phoneNumber.value?.let { number ->
            val validatePhoneNumberResult = validatePhoneNumberFormUseCase(number)
            if (!validatePhoneNumberResult.successful) {
                onContinueError.value = validatePhoneNumberResult.errorMessageId
            } else {
                val identifierPrefix = if (BuildConfig.FLAVOR == VARIANT_DEVELOP)
                    PHONE_PREFIX_RO
                else
                    PHONE_PREFIX_US
                performApiCall {
                    val request = IdentifiersRequest(
                        channel = IdentifierChannel.SMS,
                        identifier = "$identifierPrefix$number"
                    )
                    val result = submitRegistrationIdentifiersUseCase(request)
                    result.onSuccess {
                        _cmd.value = Command.SavePhoneNumber(request.identifier)
                        _baseCmd.value = BaseCommand.PerformNavAction(
                            PhoneNumberFragmentDirections.actionPhoneNumberFragmentToValidateCodeFragment(
                                CodeReceivedViaType.SMS
                            )
                        )
                    }
                    result.onError {
                        val error =
                            it.errors?.firstOrNull() ?: it.messageId ?: R.string.generic_error
                        when (it.errorCode) {
                            ERROR_CODE_422, ERROR_CODE_429 -> onContinueError.value = error
                            else -> _baseCmd.value = BaseCommand.ShowToast(error)
                        }
                    }
                }
            }
        }
    }

    sealed class Command {
        data class SavePhoneNumber(val phoneNumber: String) : Command()
    }
}
