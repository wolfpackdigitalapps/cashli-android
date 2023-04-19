package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidatePhoneNumberFormUseCase(
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePhoneNumberLengthUseCase: ValidatePhoneNumberLengthUseCase
) {

    operator fun invoke(phoneNumber: String?): ValidationResult {
        return when {
            !validatePhoneNumberUseCase(phoneNumber) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.phone_number_digits_error
                )
            !validatePhoneNumberLengthUseCase(phoneNumber) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.phone_number_length_error
                )
            else -> ValidationResult(
                successful = true
            )
        }
    }
}
