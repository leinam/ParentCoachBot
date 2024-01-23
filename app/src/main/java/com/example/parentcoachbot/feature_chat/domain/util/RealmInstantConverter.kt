package com.example.parentcoachbot.feature_chat.domain.util

import io.realm.kotlin.types.RealmInstant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset

class RealmInstantConverter {
    companion object {
        fun toLocalDate(time: RealmInstant): LocalDate {

            return LocalDateTime.ofEpochSecond(
                time.epochSeconds,
                time.nanosecondsOfSecond,
                ZoneOffset.UTC
            ).toLocalDate()
        }

        fun toLocalTime(time: RealmInstant): LocalTime {

            return LocalDateTime.ofEpochSecond(
                time.epochSeconds,
                time.nanosecondsOfSecond,
                ZoneOffset.UTC
            ).toLocalTime()
        }
    }

}