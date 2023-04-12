package com.wolfpackdigital.cashli.data.patternMatchers

import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.shared.utils.extensions.hasPasswordPattern

class PasswordPatternMatcherImpl : PatternMatcher {
    override fun checkIsValid(param: String) = param.hasPasswordPattern()
}
