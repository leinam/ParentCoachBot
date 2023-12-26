package com.example.parentcoachbot.feature_chat.domain.util

import io.realm.kotlin.types.RealmInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class RealmInstantConverter {
    companion object{
        fun toLocalDate(time: RealmInstant): LocalDate {
            val dateAsked = LocalDateTime.ofEpochSecond(
                time.epochSeconds,
                time.nanosecondsOfSecond,
                ZoneOffset.UTC
            ).toLocalDate()

            return dateAsked
        }

        fun toLocalTime(time: RealmInstant): LocalTime {
            val timeAsked = LocalDateTime.ofEpochSecond(
                time.epochSeconds,
                time.nanosecondsOfSecond,
                ZoneOffset.UTC
            ).toLocalTime()

            return timeAsked
        }
    }

}