package com.wolfpackdigital.cashli.domain.usecases.validations

import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.shared.utils.Constants

private const val ZIP_CODE_LENGTH = 5

class ValidateZipCodeUseCase(
    private val patternMatcher: PatternMatcher
) {

    operator fun invoke(params: String?): Boolean {
        return params?.let {
            when {
                !patternMatcher.checkIsValid(it) || it.length != ZIP_CODE_LENGTH -> false
                else -> true
            }
        } ?: false
    }
}