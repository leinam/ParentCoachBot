package com.example.parentcoachbot.feature_chat.domain.use_case.answerThreadUseCases

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerThreadRepository

class GetAnswerThreadByCode(private val answerThreadRepository: AnswerThreadRepository) {
    suspend operator fun invoke(code: String): AnswerThread? {
        return answerThreadRepository.getAnswerThreadByCode(answerCode = code)
    }
}