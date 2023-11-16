package com.example.parentcoachbot.feature_chat.domain.use_case.questionSessionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.QuestionSession
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedQuestionSessionsByProfile @Inject constructor(private val questionSessionRepository: QuestionSessionRepository) {
    suspend operator fun invoke(childProfileId: String): Flow<List<QuestionSession>> {
        return questionSessionRepository.getAllSavedQuestionSessionsByProfile(childProfileId)
    }
}