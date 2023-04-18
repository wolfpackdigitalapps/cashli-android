package com.wolfpackdigital.cashli.data.patternMatchers

import com.wolfpackdigital.cashli.domain.abstractions.PatternMatcher
import com.wolfpackdigital.cashli.shared.utils.extensions.hasNamePattern

class NamePatternMatcherImpl : PatternMatcher {

    override fun checkIsValid(param: String) = param.hasNamePattern()
}