package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateChoosePasswordFormUseCase(
    private val validatePasswordLengthUseCase: ValidatePasswordLengthUseCase,
    private val validateEqualFieldsUseCase: ValidateEqualFieldsUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) {
    operator fun invoke(password: String?, confirmPassword: String?): ValidationResult {
        return when {
            !validateEqualFieldsUseCase(password, confirmPassword) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.password_not_match
                )

            !validatePasswordLengthUseCase(password) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.password_length
                )

            !validatePasswordUseCase(password) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.password_format
                )
            else ->
                ValidationResult(successful = true)
        }
    }
}
