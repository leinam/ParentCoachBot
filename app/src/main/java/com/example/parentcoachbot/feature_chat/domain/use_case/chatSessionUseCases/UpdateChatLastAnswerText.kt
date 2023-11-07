package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository

class UpdateChatLastAnswerText (private val chatSessionRepository: ChatSessionRepository){
    suspend operator fun invoke(answer: Answer, chatSessionId: String){
        chatSessionRepository.updateLastAnswerText(answer, chatSessionId)
    }
}