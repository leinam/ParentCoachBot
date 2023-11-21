package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import kotlinx.coroutines.flow.Flow

class GetChatSessionsByChildProfile (private val chatSessionRepository: ChatSessionRepository){

    suspend operator fun invoke(childProfileId: String): Flow<List<ChatSession>>?{
        return chatSessionRepository.getChatSessionsByChildProfile(childProfileId = childProfileId)
    }
}