package com.wolfpackdigital.cashli.domain.entities

data class ValidationResult(
    val successful: Boolean,
    val errorMessageId: Int? = null
)
