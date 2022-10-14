package com.demonstration.table.basetable.utils

import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object DateUtils {
    private val FULL_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    private val SHORT_TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm")

    fun convertToFullDate(date: LocalDate): String {
        return FULL_DATE_FORMATTER.format(date)
    }

    fun convertToShortTime(time: LocalTime): String {
        return SHORT_TIME_FORMATTER.format(time)
    }
}