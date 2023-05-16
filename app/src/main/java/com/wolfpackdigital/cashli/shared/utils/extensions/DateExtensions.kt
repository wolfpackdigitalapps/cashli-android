package com.wolfpackdigital.cashli.shared.utils.extensions

import com.wolfpackdigital.cashli.shared.utils.Constants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

fun String?.toFormattedLocalDateTime(): String? =
    this.toLocalDateTimeOrNull()?.format(DateTimeFormatter.ofPattern(Constants.FULL_MONTH_DAY_YEAR))

fun String?.toLocalDateTimeOrNull(): LocalDateTime? =
    try {
        LocalDateTime.parse(this)
    } catch (ex: DateTimeParseException) {
        null
    }

fun String?.toLocalDateFromPatternOrNull(pattern: String?): LocalDate? =
    try {
        pattern?.let {
            LocalDate.parse(this, DateTimeFormatter.ofPattern(it))
        } ?: LocalDate.parse(this)
    } catch (ex: DateTimeParseException) {
        null
    }

fun daysBetweenDates(startDate: LocalDate, endDate: LocalDate) =
    Period.between(startDate, endDate).days
