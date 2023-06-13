package com.wolfpackdigital.cashli.domain.entities.enums

enum class Language {
    ENGLISH,
    SPANISH,
    HAITI;

    override fun toString(): String {
        return when(this) {
            ENGLISH -> "en"
            SPANISH -> "es"
            HAITI -> "ht"
        }
    }
}
