package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository

class ToggleSaveQuestionSession(private val questionSessionRepository: QuestionSessionRepository) {
    suspend operator fun invoke(questionSessionId: String): Boolean?{
        return questionSessionRepository.toggleSaveQuestionSession(questionSessionId)
    }
}