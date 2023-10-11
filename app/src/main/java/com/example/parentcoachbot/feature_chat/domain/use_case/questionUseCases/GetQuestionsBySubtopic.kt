package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

class GetQuestionsBySubtopic(private val questionRepository: QuestionRepository) {

    suspend operator fun invoke(subtopicCode: String): Flow<List<Question>> {
        return questionRepository.getQuestionsBySubtopic(subtopicCode)
    }
}