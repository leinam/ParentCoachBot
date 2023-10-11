package com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository
import org.mongodb.kbson.ObjectId

class GetAnswerThreadById(private val answerThreadRepository: AnswerThreadRepository) {
    suspend operator fun invoke(id: ObjectId): AnswerThread? {
        return answerThreadRepository.getAnswerThreadById(id = id)
    }
}