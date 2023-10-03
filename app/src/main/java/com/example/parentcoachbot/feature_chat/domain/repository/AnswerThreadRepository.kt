package com.example.parentcoachbot.feature_chat.domain.repository

import com.example.parentcoachbot.feature_chat.domain.model.AnswerThread
import org.mongodb.kbson.ObjectId

interface AnswerThreadRepository {

    suspend fun getAnswerThreadById(id: ObjectId): AnswerThread?

    suspend fun getAnswerThreadByCode(answerCode: String): AnswerThread?

    suspend fun deleteAnswerThread(id: ObjectId)

    suspend fun insertAnswerThread(answerThread: AnswerThread)


}

