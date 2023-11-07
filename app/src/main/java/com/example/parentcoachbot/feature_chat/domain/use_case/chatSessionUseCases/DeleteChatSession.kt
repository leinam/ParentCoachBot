package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository

class DeleteChatSession(private val chatSessionRepository: ChatSessionRepository) {
    suspend operator fun invoke(chatSessionId: String) {
        chatSessionRepository.deleteChatSession(chatSessionId)
    }
}