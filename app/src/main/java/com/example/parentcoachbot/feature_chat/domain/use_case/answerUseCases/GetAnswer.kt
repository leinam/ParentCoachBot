package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository
import org.mongodb.kbson.ObjectId

class GetAnswer(private val repository: AnswerRepository){
    suspend operator fun invoke(id: ObjectId): Answer? {
        return repository.getAnswerById(id)
    }
}