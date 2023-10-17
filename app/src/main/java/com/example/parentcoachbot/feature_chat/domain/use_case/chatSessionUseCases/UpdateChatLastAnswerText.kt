package com.example.parentcoachbot.feature_chat.domain.use_case.chatSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.ChatSessionRepository
import org.mongodb.kbson.ObjectId

class UpdateChatLastAnswerText (private val chatSessionRepository: ChatSessionRepository){
    suspend operator fun invoke(answer: Answer, chatSessionId: ObjectId){
        chatSessionRepository.updateLastAnswerText(answer, chatSessionId)
    }
}