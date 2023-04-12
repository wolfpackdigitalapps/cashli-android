package com.wolfpackdigital.cashli.domain.abstractions

interface PatternMatcher {
    fun checkIsValid(param: String): Boolean
}
