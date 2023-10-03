package com.example.parentcoachbot.feature_chat.domain.util

sealed class AnswerType(val code: String){
    object Info: AnswerType(code = "info")

    object Warning: AnswerType(code = "warning")
}
