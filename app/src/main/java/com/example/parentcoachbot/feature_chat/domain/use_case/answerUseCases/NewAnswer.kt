package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository

class NewAnswer(private val repository: AnswerRepository) {
    suspend operator fun invoke(answer: Answer){
        repository.insertAnswer(answer)
    }
}