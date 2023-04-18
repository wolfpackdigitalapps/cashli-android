package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.shared.utils.Constants

class ValidateNameLengthUseCase {

    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when {
                it.length >= Constants.MIN_CHARS_2 -> true
                else -> false
            }
        } ?: false
    }
}
