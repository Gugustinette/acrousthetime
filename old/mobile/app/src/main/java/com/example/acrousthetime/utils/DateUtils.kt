package com.example.acrousthetime.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class DateUtils {
    companion object {
        fun localDateTimeToDate(localDateTime: LocalDateTime): Date {
            return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        }

        @SuppressLint("SimpleDateFormat")
        fun dateToStringFormatted(date: Date): String {
            // format : yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            return format.format(date)
        }

        @SuppressLint("SimpleDateFormat")
        fun stringFormattedToDate(date: String): Date? {
            // format : yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
            val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

            return format.parse(date)
        }
    }
}