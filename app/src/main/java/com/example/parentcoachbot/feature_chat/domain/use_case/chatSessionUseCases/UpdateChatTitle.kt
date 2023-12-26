package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository

class UpdateChatTitle(private val chatSessionRepository: ChatSessionRepository){
    suspend operator fun invoke(subtopic: Subtopic, chatSessionId: String){
        chatSessionRepository.autoUpdateChatTitle(subtopic, chatSessionId)
    }
}