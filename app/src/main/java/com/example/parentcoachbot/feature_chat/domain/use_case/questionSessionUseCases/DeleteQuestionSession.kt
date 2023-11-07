package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository

class DeleteQuestionSession(private val questionSessionRepository: QuestionSessionRepository) {
    suspend operator fun invoke(questionSessionId: String){
        questionSessionRepository.deleteQuestionSession(questionSessionId)
    }
}