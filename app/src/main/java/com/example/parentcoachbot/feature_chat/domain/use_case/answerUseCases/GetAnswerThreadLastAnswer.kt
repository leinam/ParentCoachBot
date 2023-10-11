package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository

class GetAnswerThreadLastAnswer(private val answerRepository: AnswerRepository) {
    suspend operator fun invoke(answerThreadCode: String): String? {
        val answerThread = answerRepository.getAnswersByAnswerThreadCode(answerThreadCode)

        answerThread?.let {
            return it.last().answerTextEn
        }

        return null
    }
}