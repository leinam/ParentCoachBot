package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository

class GetLatestQuestionSessionByChat (private val questionSessionRepository: QuestionSessionRepository){
    suspend operator fun invoke(chatSessionId: String){
        questionSessionRepository.getLatestQuestionSessionByChatSession(chatSessionId = chatSessionId)
    }
}