package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.shared.utils.Constants

class ValidatePhoneNumberUseCase(
    private val patternMatcher: PatternMatcher
) {
    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when {
                !patternMatcher.checkIsValid(it) ||
                    params.length != Constants.PHONE_NUMBER_LENGTH -> false
                else -> true
            }
        } ?: false
    }
}
