package com.wolfpackdigital.cashli.domain.usecases.validations

private const val PASSWORD_MIN_LENGTH = 8

class ValidatePasswordLengthUseCase {
    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when {
                params.length < PASSWORD_MIN_LENGTH -> false
                else -> true
            }
        } ?: false
    }
}
