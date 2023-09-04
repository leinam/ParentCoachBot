package com.example.parentcoachbot.feature_chat.presentation.chat_screen

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.model.Question

sealed class ChatEvent{
    data class SelectQuestion(val question: Question): ChatEvent()
    data class DeleteQuestion(val question: Question) : ChatEvent()
    data class FavouriteQuestion(val question: Question): ChatEvent()
    data class SelectChat(val chatSession: ChatSession): ChatEvent()
    object RestoreQuestion: ChatEvent()
}
