package com.wolfpackdigital.cashli.shared.utils.extensions

import com.wolfpackdigital.cashli.shared.utils.Constants
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit

fun String.toFormattedLocalDateTime(): String? =
    this.toLocalDateTimeOrNull()?.format(DateTimeFormatter.ofPattern(Constants.FULL_MONTH_DAY_YEAR))

fun String.toFormattedLocalDate(pattern: String? = null): String? =
    this.toLocalDateFromPatternOrNull(pattern)?.format(DateTimeFormatter.ofPattern(Constants.FULL_MONTH_DAY_YEAR))

fun String.toLocalDateTimeOrNull(): LocalDateTime? =
    try {
        LocalDateTime.parse(this)
    } catch (ex: DateTimeParseException) {
        null
    }

fun String.toLocalDateFromPatternOrNull(pattern: String? = null): LocalDate? =
    try {
        pattern?.let {
            LocalDate.parse(this, DateTimeFormatter.ofPattern(it))
        } ?: LocalDate.parse(this)
    } catch (ex: DateTimeParseException) {
        null
    }

fun String.toFormattedZonedDate(): String? =
    this.toZonedLocalDateTimeOrNull()?.toLocalDate()
        ?.format(DateTimeFormatter.ofPattern(Constants.FULL_MONTH_DAY_YEAR))

fun Instant.toZonedLocalDateTimeOrNull(): LocalDateTime =
    LocalDateTime.ofInstant(this, ZoneOffset.UTC)

fun String.toZonedLocalDateTimeOrNull(): LocalDateTime? = toInstant()?.toZonedLocalDateTimeOrNull()

fun String.toInstant(): Instant? = try {
    Instant.parse(this)
} catch (ex: DateTimeParseException) {
    null
}

fun daysBetweenDates(startDate: LocalDate, endDate: LocalDate) =
    ChronoUnit.DAYS.between(startDate, endDate).toInt()
