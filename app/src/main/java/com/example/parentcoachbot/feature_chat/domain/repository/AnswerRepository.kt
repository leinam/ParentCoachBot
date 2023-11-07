package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer

interface AnswerRepository  {
    suspend fun getQuestionAnswers(idList: List<String>): List<Answer>

    suspend fun getAnswerById(id: String): Answer?

    suspend fun deleteAnswer(id: String)

    suspend fun insertAnswer(answer: Answer)

    suspend fun getAnswersByAnswerThreadCode(answerThreadCode: String): List<Answer>?
}