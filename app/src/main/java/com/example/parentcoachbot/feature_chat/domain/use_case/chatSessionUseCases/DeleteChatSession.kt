package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository

class DeleteChatSession(private val chatSessionRepository: ChatSessionRepository,
                        private val questionSessionRepository: QuestionSessionRepository) {
    suspend operator fun invoke(chatSessionId: String) {
        questionSessionRepository.deleteQuestionSessionsByChatSession(chatSessionId)
        chatSessionRepository.deleteChatSession(chatSessionId)
    }
}