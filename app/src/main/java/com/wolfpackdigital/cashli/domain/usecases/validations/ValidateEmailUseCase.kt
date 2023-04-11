package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher

class ValidateEmailUseCase(
    private val patternMatcher: PatternMatcher
) {
    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when {
                !patternMatcher.checkIsValid(it) -> false
                else -> true
            }
        } ?: false
    }
}
