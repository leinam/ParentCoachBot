package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow

class GetAllQuestions(private val repository: QuestionRepository) {

    suspend operator fun invoke() : Flow<List<Question>>? {
        return repository.getAllQuestions()
    }
}