package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository

class GetQuestionById(private val repository: QuestionRepository) {
    suspend operator fun invoke(questionId: String): Question? {
        return repository.getQuestionById(questionId)
    }

}