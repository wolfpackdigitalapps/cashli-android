package com.wolfpackdigital.cashli.shared.utils.extensions

import com.wolfpackdigital.cashli.shared.utils.Constants
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

fun String?.toFormattedLocalDateTime(): String? {
    val localDateTime =
        this.toLocalDateTimeOrNull() ?: this?.toZonedDateTimeOrNull()?.toLocalDateTime()
    return localDateTime?.format(DateTimeFormatter.ofPattern(Constants.FULL_MONTH_DAY_YEAR))
}

fun String?.toLocalDateTimeOrNull(): LocalDateTime? =
    try {
        LocalDateTime.parse(this)
    } catch (ex: DateTimeParseException) {
        null
    }

fun String?.toZonedDateTimeOrNull(): ZonedDateTime? =
    try {
        ZonedDateTime.parse(this)
    } catch (ex: Throwable) {
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
    ChronoUnit.DAYS.between(startDate, endDate).toInt()
