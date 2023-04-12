package com.wolfpackdigital.cashli.domain.usecases.validations

class ValidateBlankFieldUseCase {
    operator fun invoke(params: String?): Boolean {
        return when {
            params.isNullOrBlank() -> false
            else -> true
        }
    }
}
