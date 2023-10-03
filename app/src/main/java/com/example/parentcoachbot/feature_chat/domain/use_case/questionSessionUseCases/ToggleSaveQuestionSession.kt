package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import org.mongodb.kbson.ObjectId

class ToggleSaveQuestionSession(private val questionSessionRepository: QuestionSessionRepository) {
    suspend operator fun invoke(questionSessionId: ObjectId){
        questionSessionRepository.toggleSaveQuestionSession(questionSessionId)
    }
}