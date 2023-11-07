package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository

class GetAnswer(private val repository: AnswerRepository){
    suspend operator fun invoke(id: String): Answer? {
        return repository.getAnswerById(id)
    }
}