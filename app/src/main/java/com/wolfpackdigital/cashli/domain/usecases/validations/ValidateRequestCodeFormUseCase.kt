package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateRequestCodeFormUseCase(
    private val validateBlankFieldUseCase: ValidateBlankFieldUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePhoneNumberLengthUseCase: ValidatePhoneNumberLengthUseCase
) {

    operator fun invoke(
        email: String?,
        phoneNumber: String?,
        emailInUse: Boolean
    ): ValidationResult {
        return when {
            emailInUse -> {
                if (!validateBlankFieldUseCase(email))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.email_can_not_be_empty
                    )
                else if (!validateEmailUseCase(email))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.email_error
                    )
                // TODO add validation for existing account after integration with backend
                else
                    ValidationResult(true)
            }
            else -> {
                if (!validateBlankFieldUseCase(phoneNumber))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.phone_can_not_be_empty
                    )
                else if (!validatePhoneNumberLengthUseCase(phoneNumber))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.phone_number_length_error
                    )
                else if (!validatePhoneNumberUseCase(phoneNumber))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.phone_number_digits_error
                    )
                // TODO add validation for existing account after integration with backend
                else
                    ValidationResult(true)
            }
        }
    }
}
