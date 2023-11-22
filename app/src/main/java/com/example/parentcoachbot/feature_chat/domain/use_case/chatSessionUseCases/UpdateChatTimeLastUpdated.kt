package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository

class UpdateChatTimeLastUpdated (val chatSessionRepository: ChatSessionRepository){

    suspend operator fun invoke(chatSessionId: String){
        chatSessionRepository.updateTimeLastUpdated(chatSessionId = chatSessionId)
    }
}