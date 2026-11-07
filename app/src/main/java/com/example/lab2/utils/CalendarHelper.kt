package com.example.lab2.utils

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import java.util.Calendar
import java.util.TimeZone

fun addEventToCalendarSilently(context: Context, title: String, date: String, time: String) {
    if (date.isEmpty()) return

    try {
        val dateParts = date.split(".")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1
        val year = dateParts[2].toInt()

        var hour = 12
        var minute = 0
        if (time.isNotEmpty()) {
            val timeParts = time.split(":")
            hour = timeParts[0].toInt()
            minute = timeParts[1].toInt()
        }

        val startCalendar = Calendar.getInstance().apply {
            set(year, month, day, hour, minute)
        }

        val endCalendar = Calendar.getInstance().apply {
            set(year, month, day, hour, minute + 60)
        }

        val calendarId = getPrimaryCalendarId(context)
        if (calendarId == -1L) return

        val values = ContentValues().apply {
            put(CalendarContract.Events.DTSTART, startCalendar.timeInMillis)
            put(CalendarContract.Events.DTEND, endCalendar.timeInMillis)
            put(CalendarContract.Events.TITLE, title)
            put(CalendarContract.Events.CALENDAR_ID, calendarId)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        }

        context.contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)

    } catch (e: Exception) {
        println("Ошибка добавления в календарь: ${e.message}")
    }
}

private fun getPrimaryCalendarId(context: Context): Long {
    val projection = arrayOf(CalendarContract.Calendars._ID, CalendarContract.Calendars.IS_PRIMARY)
    val selection = "${CalendarContract.Calendars.IS_PRIMARY} = 1"

    context.contentResolver.query(
        CalendarContract.Calendars.CONTENT_URI,
        projection,
        selection,
        null,
        null
    )?.use { cursor ->
        if (cursor.moveToFirst()) {
            return cursor.getLong(0)
        }
    }
    return -1L
}