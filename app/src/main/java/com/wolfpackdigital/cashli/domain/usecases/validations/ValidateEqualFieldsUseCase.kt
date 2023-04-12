package com.wolfpackdigital.cashli.domain.usecases.validations

class ValidateEqualFieldsUseCase {
    operator fun invoke(firstParam: String?, secondParam: String?): Boolean {
        return when {
            firstParam != secondParam -> false
            else -> true
        }
    }
}
