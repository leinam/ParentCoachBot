package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.ChatSession
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetChatSessionsByChildProfile (private val chatSessionRepository: ChatSessionRepository){

    suspend operator fun invoke(childProfileId: ObjectId): Flow<List<ChatSession> >{
        return chatSessionRepository.getChatSessionsByChildProfile(childProfileId = childProfileId)
    }
}