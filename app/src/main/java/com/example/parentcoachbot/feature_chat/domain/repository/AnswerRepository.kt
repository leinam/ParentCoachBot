package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.Answer
import org.mongodb.kbson.ObjectId

interface AnswerRepository  {
    suspend fun getQuestionAnswers(idList: List<ObjectId>): List<Answer>

    suspend fun getAnswerById(id: ObjectId): Answer?

    suspend fun deleteAnswer(id: ObjectId)

    suspend fun insertAnswer(answer: Answer)
}