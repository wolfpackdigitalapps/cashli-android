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
                        errorMessageId = R.string.email_or_phone_can_not_be_empty
                    )
                else if (!validateEmailUseCase(email))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.wrong_email_error
                    )
                else
                    ValidationResult(true)
            }
            else -> {
                if (!validateBlankFieldUseCase(phoneNumber))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.email_or_phone_can_not_be_empty
                    )
                else if (!validatePhoneNumberUseCase(phoneNumber) ||
                    !validatePhoneNumberLengthUseCase(phoneNumber)
                )
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.wrong_phone_error
                    )
                else
                    ValidationResult(true)
            }
        }

    }
}