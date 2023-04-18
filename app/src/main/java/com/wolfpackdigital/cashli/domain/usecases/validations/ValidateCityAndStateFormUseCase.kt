package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.R
import com.wolfpackdigital.cashli.domain.entities.ValidationResult

class ValidateCityAndStateFormUseCase(
    private val validateLettersAndCommaUseCase: ValidateLettersAndCommaUseCase,
    private val validateCityAndStateUseCase: ValidateCityAndStateUseCase
) {

    operator fun invoke(cityAndState: String?): ValidationResult {
        return when {
            !validateLettersAndCommaUseCase(cityAndState) &&
                    validateCityAndStateUseCase(cityAndState) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.city_state_both_error
                )
            !validateLettersAndCommaUseCase(cityAndState) ->
                ValidationResult(
                    successful = false,
                    errorMessageId = R.string.city_and_state_error
                )
            else -> ValidationResult(true)
        }
    }
}