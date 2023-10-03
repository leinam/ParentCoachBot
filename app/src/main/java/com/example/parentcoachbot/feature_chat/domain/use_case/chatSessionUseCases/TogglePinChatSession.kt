package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases


import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import org.mongodb.kbson.ObjectId

class TogglePinChatSession(private val chatSessionRepository: ChatSessionRepository) {
    suspend operator fun invoke(chatSessionId: ObjectId) {
        chatSessionRepository.togglePinChatSession(chatSessionId)
    }
}