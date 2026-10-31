package com.jeiu.studyflow.data

import java.time.LocalDate
import java.time.temporal.ChronoUnit

object DateUtils {

    /** Days from today until [dueDate] (negative = overdue). */
    fun daysLeft(dueDate: String): Long {
        val due = runCatching { LocalDate.parse(dueDate) }.getOrNull() ?: return 0L
        return ChronoUnit.DAYS.between(LocalDate.now(), due)
    }

    fun dDayLabel(dueDate: String): String {
        val d = daysLeft(dueDate)
        return when {
            d < 0L -> "Overdue ${-d}d"
            d == 0L -> "D-day"
            else -> "D-$d"
        }
    }

    /** Due within 3 days, or overdue. */
    fun isUrgent(dueDate: String): Boolean = daysLeft(dueDate) <= 3L

    fun today(): String = LocalDate.now().toString()
}
