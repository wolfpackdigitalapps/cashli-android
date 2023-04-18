package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateFirstNameFormUseCase(
    private val validateNameUseCase: ValidateNameUseCase,
    private val validateNameLengthUseCase: ValidateNameLengthUseCase
) {

    operator fun invoke(name: String?): ValidationResult {
        return when {
            !validateNameLengthUseCase(name) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.first_name_length_error
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