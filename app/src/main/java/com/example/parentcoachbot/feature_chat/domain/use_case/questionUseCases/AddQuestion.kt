package com.example.parentcoachbot.feature_chat.domain.use_case.questionUseCases

import com.example.parentcoachbot.feature_chat.domain.model.InvalidQuestionException
import com.example.parentcoachbot.feature_chat.domain.model.Question
import com.example.parentcoachbot.feature_chat.domain.repository.QuestionRepository
import kotlin.jvm.Throws

class AddQuestion(private val repository: QuestionRepository) {

    @Throws(InvalidQuestionException::class)
    suspend operator fun invoke(question: Question){
        if (question.questionTextEn?.isBlank() == true){
            throw InvalidQuestionException(message = "The question text cannot be empty.")
        }

        repository.insertQuestion(question = question)
    }

}