package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Subtopic
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import org.mongodb.kbson.ObjectId

class UpdateChatTitle(private val chatSessionRepository: ChatSessionRepository){
    suspend operator fun invoke(subtopic: Subtopic, chatSessionId: ObjectId){
        chatSessionRepository.updateChatTitle(subtopic, chatSessionId)
    }
}