package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateSignInFormUseCase(
    private val validatePasswordLengthUseCase: ValidatePasswordLengthUseCase,
    private val validateBlankFieldUseCase: ValidateBlankFieldUseCase,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePhoneNumberUseCase: ValidatePhoneNumberUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {
    operator fun invoke(
        email: String?,
        password: String?,
        phoneNumber: String?,
        emailInUse: Boolean
    ): ValidationResult {
        return when {
            !validateBlankFieldUseCase(password) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.password_can_not_be_empty
                )
            emailInUse -> {
                if (!validateBlankFieldUseCase(email))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.email_or_phone_can_not_be_empty
                    )
                else if (!validateEmailUseCase(email) ||
                    !validatePasswordUseCase(password) ||
                    !validatePasswordLengthUseCase(password)
                )
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.incorrect_credentials_with_email
                    )
                else
                    ValidationResult(successful = true)
            }
            else -> {
                if (!validateBlankFieldUseCase(phoneNumber))
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.email_or_phone_can_not_be_empty
                    )
                else if (!validatePhoneNumberUseCase(phoneNumber) ||
                    !validatePasswordUseCase(password) ||
                    !validatePasswordLengthUseCase(password)
                ) {
                    ValidationResult(
                        successful = false,
                        errorMessageId = R.string.incorrect_credentials_with_phone
                    )
                } else
                    ValidationResult(successful = true)
            }
        }
    }
}
