package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository

class NewChatSession(private val chatSessionRepository: ChatSessionRepository) {
    suspend operator fun invoke(chatSession: ChatSession){
        chatSessionRepository.newChatSession(chatSession)
    }
}