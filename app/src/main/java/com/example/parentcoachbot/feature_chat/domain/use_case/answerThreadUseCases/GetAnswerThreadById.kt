package com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository

class GetAnswerThreadById(private val answerThreadRepository: AnswerThreadRepository) {
    suspend operator fun invoke(id: String): AnswerThread? {
        return answerThreadRepository.getAnswerThreadById(id = id)
    }
}