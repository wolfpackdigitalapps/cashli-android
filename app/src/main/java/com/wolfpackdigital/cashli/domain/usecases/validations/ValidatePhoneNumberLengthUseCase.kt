package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.shared.utils.Constants

class ValidatePhoneNumberLengthUseCase {

    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when (it.length) {
                Constants.PHONE_NUMBER_LENGTH -> true
                else -> false
            }
        } ?: false
    }
}
