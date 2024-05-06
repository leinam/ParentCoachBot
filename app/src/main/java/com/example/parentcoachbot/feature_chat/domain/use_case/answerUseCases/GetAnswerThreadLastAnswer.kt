package com.example.parentcoachbot.feature_chat.domain.use_case.answerUseCases

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import com.example.parentcoachbot.feature_chat.domain.repository.AnswerRepository

class GetAnswerThreadLastAnswer(private val answerRepository: AnswerRepository) {
    suspend operator fun invoke(answerThreadCode: String): Answer? {
        val answerThread = answerRepository.getAnswersByAnswerThreadCode(answerThreadCode)

        answerThread?.let {
            if (it.isNotEmpty()){
                return it.last()
            } else{
                return null
            }



        }

        return null
    }
}