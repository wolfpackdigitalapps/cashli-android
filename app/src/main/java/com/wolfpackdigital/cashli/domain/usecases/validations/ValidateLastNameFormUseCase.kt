package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateLastNameFormUseCase(
    private val validateNameLengthUseCase: ValidateNameLengthUseCase,
    private val validateNameUseCase: ValidateNameUseCase
) {

    operator fun invoke(name: String?): ValidationResult {
        return when {
            !validateNameLengthUseCase(name) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.last_name_length_error
                )
            !validateNameUseCase(name) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.name_error
                )
            else -> ValidationResult(true)
        }
    }
}