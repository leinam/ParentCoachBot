package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetQuestionSessionsByChatSession (private val questionSessionRepository: QuestionSessionRepository ) {

    suspend operator fun invoke(chatSessionId: ObjectId): Flow<List<QuestionSession>> {
        return questionSessionRepository.getQuestionSessionsByChatSession(chatSessionId = chatSessionId)
    }
}